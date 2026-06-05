package com.aitrich.JobPortalSystem.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompanyResponseDTO {


    private Long companyId;
    private String companyName;
    private String website;
    private String location;
    private String description;
    private boolean active;
    private String industry;
    private Integer employeeCount;
    private List<JobRequestDTO> jobs;
}
