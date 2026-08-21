package com.aitrich.JobPortalSystem.DTO;

import com.aitrich.JobPortalSystem.Enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// Step 1 of registration: user submits their details + password/confirmPassword.
// role must be JOBSEEKER or COMPANY (ADMIN cannot self-register).
@Getter
@Setter
public class RegisterInitiateDTO {

    private Role role;

    private String email;
    private String password;
    private String confirmPassword;

    // JobSeeker fields (ignored when role = COMPANY)
    private String firstName;
    private String lastName;
    private List<String> skills;
    private String location;

    // Company fields (ignored when role = JOBSEEKER)
    private String companyName;
    private String website;
    private String description;
    private String industry;
    private Integer employeeCount;
}
