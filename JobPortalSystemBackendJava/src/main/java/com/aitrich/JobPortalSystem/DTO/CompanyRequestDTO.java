package com.aitrich.JobPortalSystem.DTO;

import com.aitrich.JobPortalSystem.Entity.Job;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.bridge.Message;
import java.util.List;

@Getter
@Setter
public class CompanyRequestDTO {

    @NotNull(message = "company name must be required")
    private String companyName;

    private String email;

    private String password;

    private String website;

    private String location;

    @NotNull(message = "Required")
    private String description;

    private boolean active = true;

    private String industry;

    private Integer employeeCount;

    private List<JobRequestDTO> jobs;


}
