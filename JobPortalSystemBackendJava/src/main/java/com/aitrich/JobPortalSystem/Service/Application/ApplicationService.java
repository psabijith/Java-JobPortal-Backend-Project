package com.aitrich.JobPortalSystem.Service.Application;

import com.aitrich.JobPortalSystem.DTO.ApplicationPostDTO;
import com.aitrich.JobPortalSystem.DTO.ApplicationResponseDTO;
import com.aitrich.JobPortalSystem.Entity.Application;
import com.aitrich.JobPortalSystem.Entity.Company;
import com.aitrich.JobPortalSystem.Entity.Job;
import com.aitrich.JobPortalSystem.Entity.JobSeeker;
import com.aitrich.JobPortalSystem.Enums.Status;
import com.aitrich.JobPortalSystem.Repository.IApplicationRepo;
import com.aitrich.JobPortalSystem.Repository.ICompanyRepo;
import com.aitrich.JobPortalSystem.Repository.IJobRepo;
import com.aitrich.JobPortalSystem.Repository.IJobSeekerRepo;
import com.aitrich.JobPortalSystem.Security.OwnershipUtils;
import com.aitrich.JobPortalSystem.Service.Email.EmailService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApplicationService implements IApplicationService {

    private final IApplicationRepo applicationRepo;
    private final IJobRepo jobRepo;
    private final IJobSeekerRepo jobSeekerRepo;
    private final ICompanyRepo companyRepo;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @Override
    public ApplicationPostDTO postApplication(ApplicationPostDTO dto) {
        // Derive jobSeeker from the token — never trust the request body
        JobSeeker jobSeeker = jobSeekerRepo.findOptionalByEmail(OwnershipUtils.currentEmail())
                .orElseThrow(() -> new AccessDeniedException("Only job seekers can submit applications"));

        Application app = new Application();
        app.setAppliedDate(LocalDate.now());
        app.setStatus(Status.PENDING);
        app.setJobSeeker(jobSeeker);
        if (dto.getJobId() != null) {
            app.setJob(jobRepo.findById(dto.getJobId())
                    .orElseThrow(() -> new RuntimeException("Job not found")));
        }
        Application saved = applicationRepo.save(app);
        dto.setJobSeekerId(jobSeeker.getId());
        dto.setAppliedDate(app.getAppliedDate());
        dto.setStatus(app.getStatus());
        emailService.sendApplicationAlert("psabijith1@gmail.com",saved.getJobSeeker().getFirstName(),saved.getJob().getJobTitle());
        return dto;
    }

    @Override
    public ApplicationPostDTO setStatus(String status, Long id) {
        Application app = applicationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Application " + id + " not found"));
        // Only the company that owns the job may change status
        OwnershipUtils.check(app.getJob().getCompany().getEmail());
        app.setStatus(Status.valueOf(status));
        emailService.sendStatusUpdate(app.getJobSeeker().getEmail(),app.getJob().getJobTitle(),status);
        return modelMapper.map(applicationRepo.save(app), ApplicationPostDTO.class);
    }

    @Override
    public void deleteApplicationById(long id) {
        Application app = applicationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        // Either the applicant or the posting company may delete
        String applicantEmail = app.getJobSeeker().getEmail();
        String companyEmail   = app.getJob() != null ? app.getJob().getCompany().getEmail() : null;
        if (!OwnershipUtils.isAdmin()
                && !OwnershipUtils.currentEmail().equals(applicantEmail)
                && !OwnershipUtils.currentEmail().equals(companyEmail)) {
            throw new AccessDeniedException("You are not allowed to delete this application");
        }
        applicationRepo.deleteById(id);
    }

    @Override
    public ApplicationResponseDTO updateApplication(ApplicationPostDTO dto, Long id) {
        Application app = applicationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        OwnershipUtils.check(app.getJobSeeker().getEmail());
        app.setStatus(dto.getStatus());
        return toResponseDTO(applicationRepo.save(app));
    }

    @Override
    public ApplicationResponseDTO getApplicationById(long id) {
        Application app = applicationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with id " + id));
        String applicantEmail = app.getJobSeeker().getEmail();
        String companyEmail   = app.getJob() != null ? app.getJob().getCompany().getEmail() : null;
        if (!OwnershipUtils.isAdmin()
                && !OwnershipUtils.currentEmail().equals(applicantEmail)
                && !OwnershipUtils.currentEmail().equals(companyEmail)) {
            throw new AccessDeniedException("You are not allowed to view this application");
        }
        return toResponseDTO(app);
    }

    @Override
    public List<ApplicationResponseDTO> searchApplicationByJobSeekerId(Long id) {
        JobSeeker js = jobSeekerRepo.findById(id).orElseThrow(() -> new RuntimeException("JobSeeker not found"));
        OwnershipUtils.check(js.getEmail());
        List<Application> apps = applicationRepo.findByJobSeeker_Id(id);
        if (apps.isEmpty()) throw new RuntimeException("No applications found for job seeker id " + id);
        return apps.stream().map(this::toResponseDTO).toList();
    }

    @Override
    public List<ApplicationResponseDTO> searchApplicationByJobId(Long id) {
        Job job = jobRepo.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
        OwnershipUtils.check(job.getCompany().getEmail());
        List<Application> apps = applicationRepo.findByJob_JobId(id);
        if (apps.isEmpty()) throw new RuntimeException("No applications found for job id " + id);
        return apps.stream().map(this::toResponseDTO).toList();
    }

    @Override
    public List<ApplicationResponseDTO> getApplicationsByCompany(Long companyId) {
        Company company = companyRepo.findById(companyId).orElseThrow(() -> new RuntimeException("Company not found"));
        OwnershipUtils.check(company.getEmail());
        return applicationRepo.findByCompanyId(companyId).stream().map(this::toResponseDTO).toList();
    }

    @Override
    public List<ApplicationResponseDTO> getApplicationsByCompanyAndStatus(Long companyId, Status status) {
        Company company = companyRepo.findById(companyId).orElseThrow(() -> new RuntimeException("Company not found"));
        OwnershipUtils.check(company.getEmail());
        return applicationRepo.findByCompanyIdAndStatus(companyId, status).stream().map(this::toResponseDTO).toList();
    }

    @Override
    public List<ApplicationResponseDTO> getAllApplications() {
        if (!OwnershipUtils.isAdmin()) throw new AccessDeniedException("Admins only");
        return applicationRepo.findAll().stream().map(this::toResponseDTO).toList();
    }

    // ── no ownership check needed ─────────────────────────────────────────────

    @Override public List<ApplicationResponseDTO> getApprovedApplications() {
        return applicationRepo.findByStatus().stream().map(this::toResponseDTO).toList();
    }
    @Override public Page<ApplicationResponseDTO> getAllApplicationsPaginated(Pageable p) {
        return applicationRepo.findAll(p).map(this::toResponseDTO);
    }
    @Override public List<ApplicationResponseDTO> getApplicationsByStatus(Status status) {
        return applicationRepo.findByStatus(status).stream().map(this::toResponseDTO).toList();
    }
    @Override public List<ApplicationResponseDTO> getApplicationsByDateRange(LocalDate start, LocalDate end) {
        return applicationRepo.findByAppliedDateBetween(start, end).stream().map(this::toResponseDTO).toList();
    }
    @Override public List<ApplicationResponseDTO> getRecentApplications(int days) {
        return applicationRepo.findRecentApplications(LocalDate.now().minusDays(days)).stream().map(this::toResponseDTO).toList();
    }
    @Override public boolean hasJobSeekerApplied(Long jobSeekerId, Long jobId) {
        return applicationRepo.existsByJobSeeker_IdAndJob_JobId(jobSeekerId, jobId);
    }
    @Override public Map<String, Long> getApplicationStatistics() {
        Map<String, Long> s = new HashMap<>();
        s.put("total", applicationRepo.count());
        s.put("pending",   applicationRepo.countByStatus(Status.PENDING));
        s.put("approved",  applicationRepo.countByStatus(Status.APPROVED));
        s.put("rejected",  applicationRepo.countByStatus(Status.REJECTED));
        s.put("completed", applicationRepo.countByStatus(Status.COMPLETED));
        s.put("canceled",  applicationRepo.countByStatus(Status.CANCELED));
        return s;
    }
    @Override public long countApplicationsByStatus(Status status) { return applicationRepo.countByStatus(status); }
    @Override public List<ApplicationResponseDTO> getAllApplicationsSortedByDate() {
        return applicationRepo.findAllOrderByAppliedDateDesc().stream().map(this::toResponseDTO).toList();
    }

    private ApplicationResponseDTO toResponseDTO(Application a) {
        ApplicationResponseDTO dto = new ApplicationResponseDTO();
        dto.setId(a.getId());
        if (a.getJobSeeker() != null) dto.setJobSeekerId(a.getJobSeeker().getId());
        if (a.getJob()       != null) dto.setJobId(a.getJob().getJobId());
        dto.setStatus(a.getStatus());
        dto.setAppliedDate(a.getAppliedDate());
        return dto;
    }
}
