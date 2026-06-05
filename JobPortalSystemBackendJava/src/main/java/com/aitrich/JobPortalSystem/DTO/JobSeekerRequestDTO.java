package com.aitrich.JobPortalSystem.DTO;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobSeekerRequestDTO {
    private String firstName;
    private String lastName;
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private List<String> skills;
    private String location;
}
