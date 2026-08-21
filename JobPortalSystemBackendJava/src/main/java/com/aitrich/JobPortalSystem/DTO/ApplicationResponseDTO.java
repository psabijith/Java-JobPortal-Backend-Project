package com.aitrich.JobPortalSystem.DTO;

import com.aitrich.JobPortalSystem.Enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApplicationResponseDTO {

    private Long id;
    private Long jobSeekerId;
    private String jobSeekerName;

    private Long jobId;
    private String jobTitle;

    private String status;
    private LocalDate appliedDate;

    private String companyName;
}