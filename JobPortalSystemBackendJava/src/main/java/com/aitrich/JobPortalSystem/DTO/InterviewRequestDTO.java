package com.aitrich.JobPortalSystem.DTO;

import com.aitrich.JobPortalSystem.Enums.InterviewMode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InterviewRequestDTO {

    private Long applicationId;

    private LocalDateTime scheduledAt;

    private InterviewMode mode;

    // Meeting link (ONLINE) or venue address (OFFLINE)
    private String location;

    private String interviewerName;

    private String notes;
}
