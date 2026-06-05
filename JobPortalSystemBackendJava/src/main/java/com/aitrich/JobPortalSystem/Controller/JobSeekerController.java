package com.aitrich.JobPortalSystem.Controller;

import com.aitrich.JobPortalSystem.DTO.JobSeekerRequestDTO;
import com.aitrich.JobPortalSystem.DTO.JobSeekerResponseDTO;
import com.aitrich.JobPortalSystem.Service.JobSeeker.JobSeekerServiceImp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobseekers")
@RequiredArgsConstructor
public class JobSeekerController {

    private final JobSeekerServiceImp service;

    @PostMapping
    public ResponseEntity<JobSeekerResponseDTO> create(@Valid @RequestBody JobSeekerRequestDTO dto) {
        return ResponseEntity.ok(service.createJobSeeker(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobSeekerResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getJobSeekerById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobSeekerResponseDTO> update(@PathVariable Long id, @RequestBody JobSeekerRequestDTO dto) {
        return ResponseEntity.ok(service.updateJobSeeker(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteJobSeeker(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    @PostMapping("/{id}/uploadResume")
    public ResponseEntity<String> uploadResume(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        service.uploadResume(id, file);
        return ResponseEntity.ok("Resume uploaded successfully");
    }

    @DeleteMapping("/{id}/resume")
    public ResponseEntity<String> deleteResume(@PathVariable Long id) {
        service.deleteResume(id);
        return ResponseEntity.ok("Resume deleted successfully");
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<JobSeekerResponseDTO>> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(service.getAllJobSeekersPaginated(pageable));
    }

    @GetMapping("/active")
    public ResponseEntity<List<JobSeekerResponseDTO>> getActiveJobSeekers() {
        return ResponseEntity.ok(service.getActiveJobSeekers());
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<JobSeekerResponseDTO>> getByLocation(@PathVariable String location) {
        return ResponseEntity.ok(service.getJobSeekersByLocation(location));
    }

    @GetMapping("/skill/{skill}")
    public ResponseEntity<List<JobSeekerResponseDTO>> getBySkill(@PathVariable String skill) {
        return ResponseEntity.ok(service.getJobSeekersBySkill(skill));
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobSeekerResponseDTO>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(service.searchJobSeekersByName(name));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<JobSeekerResponseDTO> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(service.deactivateJobSeeker(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<JobSeekerResponseDTO> activate(@PathVariable Long id) {
        return ResponseEntity.ok(service.activateJobSeeker(id));
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam String email) {
        return ResponseEntity.ok(service.existsByEmail(email));
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Long>> getStatistics() {
        return ResponseEntity.ok(service.getJobSeekerStatistics());
    }

    @GetMapping("/with-resume")
    public ResponseEntity<List<JobSeekerResponseDTO>> getWithResume() {
        return ResponseEntity.ok(service.getJobSeekersWithResume());
    }

    @GetMapping("/without-resume")
    public ResponseEntity<List<JobSeekerResponseDTO>> getWithoutResume() {
        return ResponseEntity.ok(service.getJobSeekersWithoutResume());
    }
}
