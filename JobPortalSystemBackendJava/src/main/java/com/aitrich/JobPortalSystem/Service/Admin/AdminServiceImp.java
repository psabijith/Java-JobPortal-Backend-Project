package com.aitrich.JobPortalSystem.Service.Admin;

import com.aitrich.JobPortalSystem.DTO.CompanyResponseDTO;
import com.aitrich.JobPortalSystem.DTO.JobResponseDTO;
import com.aitrich.JobPortalSystem.DTO.JobSeekerResponseDTO;
import com.aitrich.JobPortalSystem.Entity.Company;
import com.aitrich.JobPortalSystem.Service.Application.IApplicationService;
import com.aitrich.JobPortalSystem.Service.Company.ICompanyService;
import com.aitrich.JobPortalSystem.Service.Job.IJobService;
import com.aitrich.JobPortalSystem.Service.JobSeeker.IJobSeekerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminServiceImp implements IAdminService {

    private final IJobSeekerService jobSeekerService;
    private final ICompanyService companyService;
    private final IJobService jobService;
    private final IApplicationService applicationService;

    @Override
    public List<JobSeekerResponseDTO> getAllJobSeekers() {
        return jobSeekerService.getAllJobSeekers();
    }

    @Override
    public void deleteJobSeeker(Long id) {
        jobSeekerService.deleteJobSeeker(id);
    }

    @Override
    public List<JobResponseDTO> getAllJobs() {
        return jobService.listAllJob();
    }

    @Override
    public void deleteJob(Long id) {
        jobService.deleteJobById(id);
    }

    @Override
    public List<CompanyResponseDTO> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @Override
    public void deleteCompany(Long id) {
        companyService.deleteCompany(id);
    }

    @Override
    public CompanyResponseDTO deactivateCompany(Long id) {
        return companyService.deactivateCompany(id);
    }

    @Override
    public CompanyResponseDTO activateCompany(Long id) {
        return companyService.activateCompany(id);
    }

    @Override
    public JobSeekerResponseDTO deactivateJobSeeker(Long id) {
        return jobSeekerService.deactivateJobSeeker(id);
    }

    @Override
    public JobSeekerResponseDTO activateJobSeeker(Long id) {
        return jobSeekerService.activateJobSeeker(id);
    }

    @Override
    public Map<String, Long> getDashboardStatistics() {
        Map<String, Long> stats = new HashMap<>();
        Map<String, Long> jobStats = jobService.getJobStatistics();
        Map<String, Long> seekerStats = jobSeekerService.getJobSeekerStatistics();
        Map<String, Long> appStats = applicationService.getApplicationStatistics();
        Map<String, Long> companyStats = companyService.getCompanyStatistics();
        stats.putAll(jobStats);
        stats.putAll(seekerStats);
        stats.putAll(appStats);
        stats.putAll(companyStats);
        return stats;
    }

    @Override
    public List<JobResponseDTO> getActiveJobs() {
        return jobService.getActiveJobs();
    }

    @Override
    public List<JobSeekerResponseDTO> getActiveJobSeekers() {
        return jobSeekerService.getActiveJobSeekers();
    }

    @Override
    public List<CompanyResponseDTO> getActiveCompanies() {
        return companyService.getActiveCompanies();
    }
}
