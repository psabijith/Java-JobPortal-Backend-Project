package com.aitrich.JobPortalSystem.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class JobRequestDTO {

    @NotNull(message = "Description must required!")
    private String description;


    private LocalDate endDate;
    private List<String> skills;
    private String experience;

    @NotNull(message = "salary must be required!")
    private Double salary;

    private Long companyId;

    private String jobTitle;
    private String jobType;
    private String location;
}
