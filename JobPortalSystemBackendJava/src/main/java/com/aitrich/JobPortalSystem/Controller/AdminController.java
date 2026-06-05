package com.aitrich.JobPortalSystem.Controller;

import com.aitrich.JobPortalSystem.DTO.CompanyResponseDTO;
import com.aitrich.JobPortalSystem.DTO.JobResponseDTO;
import com.aitrich.JobPortalSystem.DTO.JobSeekerResponseDTO;
import com.aitrich.JobPortalSystem.Entity.Company;
import com.aitrich.JobPortalSystem.Service.Admin.IAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final IAdminService adminService;

    @GetMapping("/jobseekers")
    public ResponseEntity<List<JobSeekerResponseDTO>> getAllJobSeekers() {
        return ResponseEntity.ok(adminService.getAllJobSeekers());
    }

    @DeleteMapping("/jobseeker/{id}")
    public ResponseEntity<String> deleteJobSeeker(@PathVariable Long id) {
        adminService.deleteJobSeeker(id);
        return ResponseEntity.ok("JobSeeker deleted successfully");
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobResponseDTO>> getAllJobs() {
        return ResponseEntity.ok(adminService.getAllJobs());
    }

    @DeleteMapping("/job/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        adminService.deleteJob(id);
        return ResponseEntity.ok("Job deleted successfully");
    }

    @GetMapping("/companies")
    public ResponseEntity<List<CompanyResponseDTO>> getAllCompanies() {
        return ResponseEntity.ok(adminService.getAllCompanies());
    }

    @DeleteMapping("/company/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id) {
        adminService.deleteCompany(id);
        return ResponseEntity.ok("Company deleted successfully");
    }

    @PatchMapping("/company/{id}/deactivate")
    public ResponseEntity<CompanyResponseDTO> deactivateCompany(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.deactivateCompany(id));
    }

    @PatchMapping("/company/{id}/activate")
    public ResponseEntity<CompanyResponseDTO> activateCompany(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.activateCompany(id));
    }

    @PatchMapping("/jobseeker/{id}/deactivate")
    public ResponseEntity<JobSeekerResponseDTO> deactivateJobSeeker(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.deactivateJobSeeker(id));
    }

    @PatchMapping("/jobseeker/{id}/activate")
    public ResponseEntity<JobSeekerResponseDTO> activateJobSeeker(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.activateJobSeeker(id));
    }

    @GetMapping("/dashboard/statistics")
    public ResponseEntity<Map<String, Long>> getDashboardStatistics() {
        return ResponseEntity.ok(adminService.getDashboardStatistics());
    }

    @GetMapping("/jobs/active")
    public ResponseEntity<List<JobResponseDTO>> getActiveJobs() {
        return ResponseEntity.ok(adminService.getActiveJobs());
    }

    @GetMapping("/jobseekers/active")
    public ResponseEntity<List<JobSeekerResponseDTO>> getActiveJobSeekers() {
        return ResponseEntity.ok(adminService.getActiveJobSeekers());
    }

    @GetMapping("/companies/active")
    public ResponseEntity<List<CompanyResponseDTO>> getActiveCompanies() {
        return ResponseEntity.ok(adminService.getActiveCompanies());
    }
}
