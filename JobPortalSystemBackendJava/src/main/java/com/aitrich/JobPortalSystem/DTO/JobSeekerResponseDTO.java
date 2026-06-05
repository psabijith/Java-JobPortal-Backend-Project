package com.aitrich.JobPortalSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobSeekerResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> skills;
    private String location;
    private boolean active;
    private String resumeUrl;
}
