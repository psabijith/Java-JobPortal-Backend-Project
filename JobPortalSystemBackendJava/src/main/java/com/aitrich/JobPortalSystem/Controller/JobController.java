package com.aitrich.JobPortalSystem.Controller;

import com.aitrich.JobPortalSystem.DTO.JobRequestDTO;
import com.aitrich.JobPortalSystem.DTO.JobResponseDTO;
import com.aitrich.JobPortalSystem.Service.Job.IJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final IJobService jobService;

    @PostMapping
    public ResponseEntity<JobResponseDTO> createJob(@RequestBody JobRequestDTO jobDto) {
        return new ResponseEntity<>(jobService.createJob(jobDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable long id) {
        JobResponseDTO jobDTO = jobService.getJobById(id);
        if (jobDTO == null) {
            return ResponseEntity.status(404).body("job not found!!!");
        }
        return ResponseEntity.ok(jobDTO);
    }

    @GetMapping
    public ResponseEntity<List<JobResponseDTO>> getAllJobs() {
        return ResponseEntity.ok(jobService.listAllJob());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@PathVariable long id, @RequestBody JobRequestDTO jobDto) {
        JobResponseDTO jobDTO = jobService.getJobById(id);
        if (jobDTO == null) {
            return ResponseEntity.status(404).body("job not found!!!");
        }
        return ResponseEntity.ok(jobService.updateJob(id, jobDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable long id) {
        JobResponseDTO jobDTO = jobService.getJobById(id);
        if (jobDTO == null) {
            return ResponseEntity.status(404).body("job not found!!!");
        }
        jobService.deleteJobById(id);
        return ResponseEntity.ok("Job deleted successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobResponseDTO>> searchJob(@RequestParam String keyword) {
        List<JobResponseDTO> jobDTO = jobService.searchJob(keyword);
        if (jobDTO.isEmpty()) {
            return ResponseEntity.status(404).body(Collections.emptyList());
        }
        return ResponseEntity.ok(jobDTO);
    }

    @PostMapping("/{jobId}/save/{jobSeekerId}")
    public ResponseEntity<String> saveJobToProfile(@PathVariable Long jobId, @PathVariable Long jobSeekerId) {
        jobService.saveAJobToProfile(jobId, jobSeekerId);
        return ResponseEntity.ok("Job saved successfully");
    }

    @DeleteMapping("/{jobId}/unsave/{jobSeekerId}")
    public ResponseEntity<String> removeSavedJob(@PathVariable Long jobId, @PathVariable Long jobSeekerId) {
        jobService.removeSavedJobFromProfile(jobId, jobSeekerId);
        return ResponseEntity.ok("Saved job removed successfully");
    }

    @GetMapping("/saved/{jobSeekerId}")
    public ResponseEntity<List<JobResponseDTO>> getSavedJobs(@PathVariable Long jobSeekerId) {
        return ResponseEntity.ok(jobService.getSavedJobFromProfile(jobSeekerId));
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<JobResponseDTO>> getJobsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "jobId") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(jobService.getAllJobsPaginated(pageable));
    }

    @GetMapping("/active")
    public ResponseEntity<List<JobResponseDTO>> getActiveJobs() {
        return ResponseEntity.ok(jobService.getActiveJobs());
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<JobResponseDTO>> getJobsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(jobService.getJobsByCompany(companyId));
    }

    @GetMapping("/salary-range")
    public ResponseEntity<List<JobResponseDTO>> getJobsBySalaryRange(
            @RequestParam double min,
            @RequestParam double max) {
        return ResponseEntity.ok(jobService.getJobsBySalaryRange(min, max));
    }



    @GetMapping("/recent")
    public ResponseEntity<List<JobResponseDTO>> getRecentJobs(
            @RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(jobService.getRecentJobs(days));
    }

    @GetMapping("/min-salary")
    public ResponseEntity<List<JobResponseDTO>> getJobsByMinimumSalary(@RequestParam double minSalary) {
        return ResponseEntity.ok(jobService.getJobsByMinimumSalary(minSalary));
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<JobResponseDTO> setJobStatus(@PathVariable long id) {
        return ResponseEntity.ok(jobService.setJobActiveStatus(id));
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Long>> getJobStatistics() {
        return ResponseEntity.ok(jobService.getJobStatistics());
    }

    @GetMapping("/count/active")
    public ResponseEntity<Long> countActiveJobs() {
        return ResponseEntity.ok(jobService.countActiveJobs());
    }
}
