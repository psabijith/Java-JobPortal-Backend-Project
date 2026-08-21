package com.aitrich.JobPortalSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ApplicationDetailsDTO {

    private Long applicationId;
    private String status;
    private LocalDate appliedDate;

    private Long jobId;
    private String jobTitle;

    private Long jobSeekerId;
    private String jobSeekerName;
    private String email;
    private String location;
    private String resumeUrl;

    private String companyName;
}
