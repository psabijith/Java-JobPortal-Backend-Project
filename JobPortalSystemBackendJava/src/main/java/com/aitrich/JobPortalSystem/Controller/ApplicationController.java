package com.aitrich.JobPortalSystem.Controller;

import com.aitrich.JobPortalSystem.DTO.ApplicationDetailsDTO;
import com.aitrich.JobPortalSystem.DTO.ApplicationPostDTO;
import com.aitrich.JobPortalSystem.DTO.ApplicationResponseDTO;
import com.aitrich.JobPortalSystem.Enums.Status;
import com.aitrich.JobPortalSystem.Service.Application.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationPostDTO> postApplication(@RequestBody ApplicationPostDTO applicationPostDTO) {
        return ResponseEntity.ok(applicationService.postApplication(applicationPostDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDTO> getApplicationById(@PathVariable long id) {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<ApplicationDetailsDTO> getApplicationDetails(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getApplicationDetails(id));
    }

    @GetMapping
    public ResponseEntity<List<ApplicationResponseDTO>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponseDTO> updateApplication(
            @RequestBody ApplicationPostDTO applicationDTO, @PathVariable long id) {
        return ResponseEntity.ok(applicationService.updateApplication(applicationDTO, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApplication(@PathVariable long id) {
        applicationService.deleteApplicationById(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationResponseDTO>> getApplicationsByJobId(@PathVariable long jobId) {
        return ResponseEntity.ok(applicationService.searchApplicationByJobId(jobId));
    }

    @GetMapping("/jobseeker/{jobId}")
    public ResponseEntity<List<ApplicationResponseDTO>> getApplicationByJobSeekerId(@PathVariable long jobId) {
        return ResponseEntity.ok(applicationService.searchApplicationByJobSeekerId(jobId));
    }

    @PutMapping("/{id}/{status}")
    public ResponseEntity<ApplicationPostDTO> setStatus(@PathVariable long id , @PathVariable String status) {
        return ResponseEntity.ok(applicationService.setStatus(status, id));
    }

    @GetMapping("/approved")
    public ResponseEntity<List<ApplicationResponseDTO>> getApplicationsByApproved() {
        return ResponseEntity.ok(applicationService.getApprovedApplications());
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<ApplicationResponseDTO>> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(applicationService.getAllApplicationsPaginated(pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ApplicationResponseDTO>> getByStatus(@PathVariable Status status) {
        return ResponseEntity.ok(applicationService.getApplicationsByStatus(status));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<ApplicationResponseDTO>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(applicationService.getApplicationsByDateRange(start, end));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<ApplicationResponseDTO>> getRecentApplications(
            @RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(applicationService.getRecentApplications(days));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> hasJobSeekerApplied(
            @RequestParam Long jobSeekerId,
            @RequestParam Long jobId) {
        return ResponseEntity.ok(applicationService.hasJobSeekerApplied(jobSeekerId, jobId));
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Long>> getApplicationStatistics() {
        return ResponseEntity.ok(applicationService.getApplicationStatistics());
    }

    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countByStatus(@PathVariable Status status) {
        return ResponseEntity.ok(applicationService.countApplicationsByStatus(status));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<ApplicationResponseDTO>> getByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(applicationService.getApplicationsByCompany(companyId));
    }

    @GetMapping("/company/{companyId}/status/{status}")
    public ResponseEntity<List<ApplicationResponseDTO>> getByCompanyAndStatus(
            @PathVariable Long companyId,
            @PathVariable Status status) {
        return ResponseEntity.ok(applicationService.getApplicationsByCompanyAndStatus(companyId, status));
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<ApplicationResponseDTO>> getAllSortedByDate() {
        return ResponseEntity.ok(applicationService.getAllApplicationsSortedByDate());
    }
}
