package com.aitrich.JobPortalSystem.Service.Application;

import com.aitrich.JobPortalSystem.DTO.ApplicationPostDTO;
import com.aitrich.JobPortalSystem.DTO.ApplicationResponseDTO;
import com.aitrich.JobPortalSystem.Enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IApplicationService {

    ApplicationPostDTO postApplication(ApplicationPostDTO applicationPostDTO);

    ApplicationResponseDTO getApplicationById(long id);

    List<ApplicationResponseDTO> getAllApplications();

    ApplicationResponseDTO updateApplication(ApplicationPostDTO applicationDTO, Long id);

    void deleteApplicationById(long id);

    List<ApplicationResponseDTO> searchApplicationByJobId(Long id);

    List<ApplicationResponseDTO> searchApplicationByJobSeekerId(Long id);

    ApplicationPostDTO setStatus(String status, Long id);

    List<ApplicationResponseDTO> getApprovedApplications();

    // Additional methods
    Page<ApplicationResponseDTO> getAllApplicationsPaginated(Pageable pageable);

    List<ApplicationResponseDTO> getApplicationsByStatus(Status status);

    List<ApplicationResponseDTO> getApplicationsByDateRange(LocalDate start, LocalDate end);

    List<ApplicationResponseDTO> getRecentApplications(int days);

    boolean hasJobSeekerApplied(Long jobSeekerId, Long jobId);

    Map<String, Long> getApplicationStatistics();

    long countApplicationsByStatus(Status status);

    List<ApplicationResponseDTO> getApplicationsByCompany(Long companyId);

    List<ApplicationResponseDTO> getApplicationsByCompanyAndStatus(Long companyId, Status status);

    List<ApplicationResponseDTO> getAllApplicationsSortedByDate();
}
