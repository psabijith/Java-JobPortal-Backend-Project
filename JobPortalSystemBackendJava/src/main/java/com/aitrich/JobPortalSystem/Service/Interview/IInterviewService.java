package com.aitrich.JobPortalSystem.Service.Interview;

import com.aitrich.JobPortalSystem.DTO.InterviewRequestDTO;
import com.aitrich.JobPortalSystem.DTO.InterviewResponseDTO;

import java.util.List;

public interface IInterviewService {

    InterviewResponseDTO scheduleInterview(InterviewRequestDTO dto);

    InterviewResponseDTO getInterviewById(Long id);

    List<InterviewResponseDTO> getAllInterviews();

    List<InterviewResponseDTO> getInterviewsByApplication(Long applicationId);

    List<InterviewResponseDTO> getInterviewsByJobSeeker(Long jobSeekerId);

    List<InterviewResponseDTO> getInterviewsByCompany(Long companyId);

    InterviewResponseDTO updateInterview(Long id, InterviewRequestDTO dto);

    InterviewResponseDTO updateStatus(Long id, String status);

    InterviewResponseDTO submitFeedback(Long id, String feedback);

    void cancelInterview(Long id);

    void deleteInterview(Long id);
}
