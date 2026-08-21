package com.aitrich.JobPortalSystem.DTO;

import com.aitrich.JobPortalSystem.Enums.InterviewMode;
import com.aitrich.JobPortalSystem.Enums.InterviewStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InterviewResponseDTO {

    private Long id;

    private Long applicationId;
    private Long jobId;
    private String jobTitle;
    private Long jobSeekerId;
    private String jobSeekerName;
    private String companyName;

    private LocalDateTime scheduledAt;
    private InterviewMode mode;
    private String location;
    private String interviewerName;
    private InterviewStatus status;
    private String notes;
    private String feedback;
}
