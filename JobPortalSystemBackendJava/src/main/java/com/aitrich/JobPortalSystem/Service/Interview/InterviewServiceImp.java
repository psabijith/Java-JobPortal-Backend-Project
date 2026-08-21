package com.aitrich.JobPortalSystem.Service.Interview;

import com.aitrich.JobPortalSystem.DTO.InterviewRequestDTO;
import com.aitrich.JobPortalSystem.DTO.InterviewResponseDTO;
import com.aitrich.JobPortalSystem.Entity.Application;
import com.aitrich.JobPortalSystem.Entity.Interview;
import com.aitrich.JobPortalSystem.Enums.InterviewStatus;
import com.aitrich.JobPortalSystem.Repository.IApplicationRepo;
import com.aitrich.JobPortalSystem.Repository.IInterviewRepo;
import com.aitrich.JobPortalSystem.Service.Email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewServiceImp implements IInterviewService {

    private final IInterviewRepo interviewRepo;
    private final IApplicationRepo applicationRepo;
    private final EmailService emailService;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a");

    @Override
    public InterviewResponseDTO scheduleInterview(InterviewRequestDTO dto) {
        Application application = applicationRepo.findById(dto.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Application not found"));

        Interview interview = new Interview();
        interview.setApplication(application);
        interview.setScheduledAt(dto.getScheduledAt());
        interview.setMode(dto.getMode());
        interview.setLocation(dto.getLocation());
        interview.setInterviewerName(dto.getInterviewerName());
        interview.setNotes(dto.getNotes());
        interview.setStatus(InterviewStatus.SCHEDULED);
        interview.setCreatedAt(LocalDateTime.now());

        Interview saved = interviewRepo.save(interview);

        emailService.sendInterviewScheduled(
                application.getJobSeeker().getEmail(),
                application.getJobSeeker().getFirstName(),
                application.getJob() != null ? application.getJob().getJobTitle() : "the position",
                saved.getScheduledAt().format(DATE_FORMAT),
                saved.getMode().toString(),
                saved.getLocation()
        );

        return toDTO(saved);
    }

    @Override
    public InterviewResponseDTO getInterviewById(Long id) {
        Interview interview = interviewRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found"));
        return toDTO(interview);
    }

    @Override
    public List<InterviewResponseDTO> getAllInterviews() {
        return interviewRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<InterviewResponseDTO> getInterviewsByApplication(Long applicationId) {
        return interviewRepo.findByApplication_Id(applicationId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<InterviewResponseDTO> getInterviewsByJobSeeker(Long jobSeekerId) {
        return interviewRepo.findByApplication_JobSeeker_Id(jobSeekerId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<InterviewResponseDTO> getInterviewsByCompany(Long companyId) {
        return interviewRepo.findByCompanyId(companyId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public InterviewResponseDTO updateInterview(Long id, InterviewRequestDTO dto) {
        Interview interview = interviewRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        boolean reschedule = dto.getScheduledAt() != null && !dto.getScheduledAt().equals(interview.getScheduledAt());

        if (dto.getScheduledAt() != null) interview.setScheduledAt(dto.getScheduledAt());
        if (dto.getMode() != null) interview.setMode(dto.getMode());
        if (dto.getLocation() != null) interview.setLocation(dto.getLocation());
        if (dto.getInterviewerName() != null) interview.setInterviewerName(dto.getInterviewerName());
        if (dto.getNotes() != null) interview.setNotes(dto.getNotes());

        if (reschedule) {
            interview.setStatus(InterviewStatus.RESCHEDULED);
        }

        Interview saved = interviewRepo.save(interview);

        if (reschedule) {
            emailService.sendInterviewScheduled(
                    saved.getApplication().getJobSeeker().getEmail(),
                    saved.getApplication().getJobSeeker().getFirstName(),
                    saved.getApplication().getJob() != null ? saved.getApplication().getJob().getJobTitle() : "the position",
                    saved.getScheduledAt().format(DATE_FORMAT),
                    saved.getMode().toString(),
                    saved.getLocation()
            );
        }

        return toDTO(saved);
    }

    @Override
    public InterviewResponseDTO updateStatus(Long id, String status) {
        Interview interview = interviewRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        InterviewStatus newStatus;
        try {
            newStatus = InterviewStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid interview status: " + status);
        }

        interview.setStatus(newStatus);
        Interview saved = interviewRepo.save(interview);

        emailService.sendInterviewStatusUpdate(
                saved.getApplication().getJobSeeker().getEmail(),
                saved.getApplication().getJob() != null ? saved.getApplication().getJob().getJobTitle() : "the position",
                saved.getStatus().toString()
        );

        return toDTO(saved);
    }

    @Override
    public InterviewResponseDTO submitFeedback(Long id, String feedback) {
        Interview interview = interviewRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found"));
        interview.setFeedback(feedback);
        interview.setStatus(InterviewStatus.COMPLETED);
        return toDTO(interviewRepo.save(interview));
    }

    @Override
    public void cancelInterview(Long id) {
        Interview interview = interviewRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview not found"));
        interview.setStatus(InterviewStatus.CANCELLED);
        Interview saved = interviewRepo.save(interview);

        emailService.sendInterviewStatusUpdate(
                saved.getApplication().getJobSeeker().getEmail(),
                saved.getApplication().getJob() != null ? saved.getApplication().getJob().getJobTitle() : "the position",
                InterviewStatus.CANCELLED.toString()
        );
    }

    @Override
    public void deleteInterview(Long id) {
        if (!interviewRepo.existsById(id)) {
            throw new RuntimeException("Interview not found");
        }
        interviewRepo.deleteById(id);
    }

    private InterviewResponseDTO toDTO(Interview interview) {
        Application app = interview.getApplication();
        InterviewResponseDTO dto = new InterviewResponseDTO();
        dto.setId(interview.getId());
        dto.setApplicationId(app.getId());
        dto.setScheduledAt(interview.getScheduledAt());
        dto.setMode(interview.getMode());
        dto.setLocation(interview.getLocation());
        dto.setInterviewerName(interview.getInterviewerName());
        dto.setStatus(interview.getStatus());
        dto.setNotes(interview.getNotes());
        dto.setFeedback(interview.getFeedback());

        if (app.getJob() != null) {
            dto.setJobId(app.getJob().getJobId());
            dto.setJobTitle(app.getJob().getJobTitle());
            if (app.getJob().getCompany() != null) {
                dto.setCompanyName(app.getJob().getCompany().getCompanyName());
            }
        }
        if (app.getJobSeeker() != null) {
            dto.setJobSeekerId(app.getJobSeeker().getId());
            dto.setJobSeekerName(app.getJobSeeker().getFirstName() + " " + app.getJobSeeker().getLastName());
        }
        return dto;
    }
}
