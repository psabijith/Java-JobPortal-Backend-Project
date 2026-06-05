package com.aitrich.JobPortalSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class JobResponseDTO {

    private Long jobId;
    private String description;
    private LocalDate postedDate;
    private LocalDate endDate;
    private List<String> skills;
    private String experience;
    private Double salary;
    private String companyName;
    private boolean active;
    private String jobTitle;
    private String jobType;
    private String location;
}
