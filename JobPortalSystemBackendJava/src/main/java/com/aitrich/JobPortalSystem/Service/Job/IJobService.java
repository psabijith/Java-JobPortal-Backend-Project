package com.aitrich.JobPortalSystem.Service.Job;

import com.aitrich.JobPortalSystem.DTO.JobRequestDTO;
import com.aitrich.JobPortalSystem.DTO.JobResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IJobService {

    JobResponseDTO createJob(JobRequestDTO jobDto);

    void deleteJobById(long id);

    JobResponseDTO getJobById(long id);

    List<JobResponseDTO> listAllJob();

    JobResponseDTO updateJob(long id, JobRequestDTO updatedJob);



    List<JobResponseDTO> searchJob(String keyword);

    void saveAJobToProfile(Long jobId, Long jobSeekerId);

    void removeSavedJobFromProfile(Long jobId, Long jobSeekerId);

    List<JobResponseDTO> getSavedJobFromProfile(Long jobSeekerId);

    // Additional methods
    Page<JobResponseDTO> getAllJobsPaginated(Pageable pageable);

    List<JobResponseDTO> getActiveJobs();

    List<JobResponseDTO> getJobsByCompany(Long companyId);

    List<JobResponseDTO> getJobsBySalaryRange(double min, double max);
 List<JobResponseDTO> getRecentJobs(int days);

    List<JobResponseDTO> getJobsByMinimumSalary(double minSalary);

    JobResponseDTO setJobActiveStatus(long id);

    Map<String, Long> getJobStatistics();

    long countActiveJobs();
}
