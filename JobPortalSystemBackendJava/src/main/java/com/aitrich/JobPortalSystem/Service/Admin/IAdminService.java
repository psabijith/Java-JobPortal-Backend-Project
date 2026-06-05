package com.aitrich.JobPortalSystem.Service.Admin;

import com.aitrich.JobPortalSystem.DTO.CompanyResponseDTO;
import com.aitrich.JobPortalSystem.DTO.JobResponseDTO;
import com.aitrich.JobPortalSystem.DTO.JobSeekerResponseDTO;
import com.aitrich.JobPortalSystem.Entity.Company;

import java.util.List;
import java.util.Map;

public interface IAdminService {

    List<JobSeekerResponseDTO> getAllJobSeekers();

    void deleteJobSeeker(Long id);

    List<JobResponseDTO> getAllJobs();

    void deleteJob(Long id);

    // Additional admin methods
    List<CompanyResponseDTO> getAllCompanies();

    void deleteCompany(Long id);

    CompanyResponseDTO deactivateCompany(Long id);

    CompanyResponseDTO activateCompany(Long id);

    JobSeekerResponseDTO deactivateJobSeeker(Long id);

    JobSeekerResponseDTO activateJobSeeker(Long id);

    Map<String, Long> getDashboardStatistics();

    List<JobResponseDTO> getActiveJobs();

    List<JobSeekerResponseDTO> getActiveJobSeekers();

    List<CompanyResponseDTO> getActiveCompanies();
}
