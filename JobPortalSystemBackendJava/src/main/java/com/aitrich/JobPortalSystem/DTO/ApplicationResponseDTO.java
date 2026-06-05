package com.aitrich.JobPortalSystem.DTO;

import com.aitrich.JobPortalSystem.Enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ApplicationResponseDTO {

    private Long id;
    private Long jobSeekerId;
    private Long jobId;
    private Status status;
    private LocalDate appliedDate;
}