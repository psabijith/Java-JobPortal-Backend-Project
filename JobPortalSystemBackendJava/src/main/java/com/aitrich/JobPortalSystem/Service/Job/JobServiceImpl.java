package com.aitrich.JobPortalSystem.Service.Job;

import com.aitrich.JobPortalSystem.DTO.JobRequestDTO;
import com.aitrich.JobPortalSystem.DTO.JobResponseDTO;
import com.aitrich.JobPortalSystem.Entity.Company;
import com.aitrich.JobPortalSystem.Entity.Job;
import com.aitrich.JobPortalSystem.Entity.JobSeeker;
import com.aitrich.JobPortalSystem.Repository.ICompanyRepo;
import com.aitrich.JobPortalSystem.Repository.IJobRepo;
import com.aitrich.JobPortalSystem.Repository.IJobSeekerRepo;
import com.aitrich.JobPortalSystem.Security.OwnershipUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements IJobService {

    private final IJobRepo jobRepo;
    private final ICompanyRepo companyRepo;
    private final ModelMapper modelMapper;
    private final IJobSeekerRepo jobSeekerRepo;

    @Override
    public JobResponseDTO createJob(JobRequestDTO dto) {
        Company company = companyRepo.findById(dto.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
        OwnershipUtils.check(company.getEmail());

        Job job = modelMapper.map(dto, Job.class);
        job.setPostedDate(LocalDate.now());
        job.setActive(true);
        job.setCompany(company);
        return convertToDTO(jobRepo.save(job));
    }

    @Override
    public JobResponseDTO updateJob(long id, JobRequestDTO dto) {
        Job job = jobRepo.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
        OwnershipUtils.check(job.getCompany().getEmail());

        Company company = companyRepo.findById(dto.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
        job.setCompany(company);
        job.setDescription(dto.getDescription());
        job.setSkills(dto.getSkills());
        job.setExperience(dto.getExperience());
        job.setSalary(dto.getSalary());
        job.setPostedDate(LocalDate.now());
        job.setEndDate(dto.getEndDate());
        job.setJobTitle(dto.getJobTitle());
        job.setJobType(dto.getJobType());
        job.setLocation(dto.getLocation());
        return convertToDTO(jobRepo.save(job));
    }

    @Override
    public void deleteJobById(long id) {
        Job job = jobRepo.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
        OwnershipUtils.check(job.getCompany().getEmail());
        jobRepo.deleteById(id);
    }

    @Override
    public JobResponseDTO setJobActiveStatus(long id) {
        Job job = jobRepo.findById(id).orElseThrow(() -> new RuntimeException("Job not found with id " + id));
        OwnershipUtils.check(job.getCompany().getEmail());
        job.setActive(!job.isActive());
        return convertToDTO(jobRepo.save(job));
    }

    @Override
    public void saveAJobToProfile(Long jobId, Long jobSeekerId) {
        JobSeeker js = jobSeekerRepo.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("JobSeeker not found with id " + jobSeekerId));
        OwnershipUtils.check(js.getEmail());

        Job job = jobRepo.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found with id " + jobId));
        if (!js.getSavedJobs().contains(job)) { js.getSavedJobs().add(job); jobSeekerRepo.save(js); }
    }

    @Override
    public void removeSavedJobFromProfile(Long jobId, Long jobSeekerId) {
        JobSeeker js = jobSeekerRepo.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("JobSeeker not found with id " + jobSeekerId));
        OwnershipUtils.check(js.getEmail());

        Job job = jobRepo.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found with id " + jobId));
        js.getSavedJobs().remove(job);
        jobSeekerRepo.save(js);
    }

    @Override
    public List<JobResponseDTO> getSavedJobFromProfile(Long jobSeekerId) {
        JobSeeker js = jobSeekerRepo.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("JobSeeker not found with id " + jobSeekerId));
        OwnershipUtils.check(js.getEmail());
        return js.getSavedJobs().stream().map(this::convertToDTO).toList();
    }

    // ── read-only ─────────────────────────────────────────────────────────────

    @Override public JobResponseDTO getJobById(long id) {
        return convertToDTO(jobRepo.findById(id).orElseThrow(() -> new RuntimeException("Job not found")));
    }
    @Override public List<JobResponseDTO> listAllJob() {
        return jobRepo.findAll().stream().map(this::convertToDTO).toList();
    }
    @Override public List<JobResponseDTO> searchJob(String keyword) {
        return jobRepo.searchJobs(keyword).stream().map(this::convertToDTO).toList();
    }
    @Override public Page<JobResponseDTO> getAllJobsPaginated(Pageable p) {
        return jobRepo.findAll(p).map(this::convertToDTO);
    }
    @Override public List<JobResponseDTO> getActiveJobs() {
        return jobRepo.findActiveJobsNotExpired(LocalDate.now()).stream().map(this::convertToDTO).toList();
    }
    @Override public List<JobResponseDTO> getJobsByCompany(Long companyId) {
        return jobRepo.findByCompany_Id(companyId).stream().map(this::convertToDTO).toList();
    }
    @Override public List<JobResponseDTO> getJobsBySalaryRange(double min, double max) {
        return jobRepo.findBySalaryRange(min, max).stream().map(this::convertToDTO).toList();
    }
    @Override public List<JobResponseDTO> getRecentJobs(int days) {
        return jobRepo.findRecentJobs(LocalDate.now().minusDays(days)).stream().map(this::convertToDTO).toList();
    }
    @Override public List<JobResponseDTO> getJobsByMinimumSalary(double minSalary) {
        return jobRepo.findJobsWithMinimumSalary(minSalary).stream().map(this::convertToDTO).toList();
    }
    @Override public Map<String, Long> getJobStatistics() {
        Map<String, Long> s = new HashMap<>();
        s.put("totalJobs", jobRepo.count());
        s.put("activeJobs", jobRepo.countByActive(true));
        s.put("inactiveJobs", jobRepo.countByActive(false));
        return s;
    }
    @Override public long countActiveJobs() { return jobRepo.countByActive(true); }

    private JobResponseDTO convertToDTO(Job job) {
        JobResponseDTO dto = new JobResponseDTO();
        dto.setJobId(job.getJobId()); dto.setDescription(job.getDescription());
        dto.setPostedDate(job.getPostedDate()); dto.setEndDate(job.getEndDate());
        dto.setSkills(job.getSkills()); dto.setExperience(job.getExperience());
        dto.setSalary(job.getSalary()); dto.setActive(job.isActive());
        dto.setJobTitle(job.getJobTitle()); dto.setJobType(job.getJobType());
        dto.setLocation(job.getLocation());
        if (job.getCompany() != null) dto.setCompanyName(job.getCompany().getCompanyName());
        return dto;
    }
}
