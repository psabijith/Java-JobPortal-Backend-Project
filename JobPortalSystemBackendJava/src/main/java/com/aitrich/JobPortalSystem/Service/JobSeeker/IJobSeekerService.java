package com.aitrich.JobPortalSystem.Service.JobSeeker;

import com.aitrich.JobPortalSystem.DTO.JobSeekerRequestDTO;
import com.aitrich.JobPortalSystem.DTO.JobSeekerResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IJobSeekerService {

    JobSeekerResponseDTO createJobSeeker(JobSeekerRequestDTO dto);

    JobSeekerResponseDTO getJobSeekerById(Long id);

    List<JobSeekerResponseDTO> getAllJobSeekers();

    JobSeekerResponseDTO updateJobSeeker(Long id, JobSeekerRequestDTO dto);

    void deleteJobSeeker(Long id);

    void uploadResume(Long id, MultipartFile file);

    void deleteResume(Long id);

    // Additional methods
    Page<JobSeekerResponseDTO> getAllJobSeekersPaginated(Pageable pageable);

    List<JobSeekerResponseDTO> getActiveJobSeekers();

    List<JobSeekerResponseDTO> getJobSeekersByLocation(String location);

    List<JobSeekerResponseDTO> getJobSeekersBySkill(String skill);

    List<JobSeekerResponseDTO> searchJobSeekersByName(String name);

    JobSeekerResponseDTO deactivateJobSeeker(Long id);

    JobSeekerResponseDTO activateJobSeeker(Long id);

    boolean existsByEmail(String email);

    Map<String, Long> getJobSeekerStatistics();

    List<JobSeekerResponseDTO> getJobSeekersWithResume();

    List<JobSeekerResponseDTO> getJobSeekersWithoutResume();
}
