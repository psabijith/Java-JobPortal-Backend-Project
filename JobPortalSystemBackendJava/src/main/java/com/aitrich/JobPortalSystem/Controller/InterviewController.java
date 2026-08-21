package com.aitrich.JobPortalSystem.Controller;

import com.aitrich.JobPortalSystem.DTO.InterviewRequestDTO;
import com.aitrich.JobPortalSystem.DTO.InterviewResponseDTO;
import com.aitrich.JobPortalSystem.Service.Interview.IInterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final IInterviewService interviewService;

    // Company/Admin schedules an interview for a given application
    @PostMapping
    public ResponseEntity<InterviewResponseDTO> scheduleInterview(@RequestBody InterviewRequestDTO dto) {
        return ResponseEntity.ok(interviewService.scheduleInterview(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewResponseDTO> getInterviewById(@PathVariable Long id) {
        return ResponseEntity.ok(interviewService.getInterviewById(id));
    }

    @GetMapping
    public ResponseEntity<List<InterviewResponseDTO>> getAllInterviews() {
        return ResponseEntity.ok(interviewService.getAllInterviews());
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<InterviewResponseDTO>> getByApplication(@PathVariable Long applicationId) {
        return ResponseEntity.ok(interviewService.getInterviewsByApplication(applicationId));
    }

    @GetMapping("/jobseeker/{jobSeekerId}")
    public ResponseEntity<List<InterviewResponseDTO>> getByJobSeeker(@PathVariable Long jobSeekerId) {
        return ResponseEntity.ok(interviewService.getInterviewsByJobSeeker(jobSeekerId));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<InterviewResponseDTO>> getByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(interviewService.getInterviewsByCompany(companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InterviewResponseDTO> updateInterview(
            @PathVariable Long id, @RequestBody InterviewRequestDTO dto) {
        return ResponseEntity.ok(interviewService.updateInterview(id, dto));
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<InterviewResponseDTO> updateStatus(
            @PathVariable Long id, @PathVariable String status) {
        return ResponseEntity.ok(interviewService.updateStatus(id, status));
    }

    @PutMapping("/{id}/feedback")
    public ResponseEntity<InterviewResponseDTO> submitFeedback(
            @PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(interviewService.submitFeedback(id, body.get("feedback")));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelInterview(@PathVariable Long id) {
        interviewService.cancelInterview(id);
        return ResponseEntity.ok("Interview cancelled successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInterview(@PathVariable Long id) {
        interviewService.deleteInterview(id);
        return ResponseEntity.ok("Interview deleted successfully");
    }
}
