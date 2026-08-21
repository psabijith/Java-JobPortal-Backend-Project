package com.aitrich.JobPortalSystem.Entity;

import com.aitrich.JobPortalSystem.Enums.InterviewMode;
import com.aitrich.JobPortalSystem.Enums.InterviewStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "interviews")
@Getter
@Setter
@NoArgsConstructor
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Every interview belongs to exactly one job application.
    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    @JsonIgnoreProperties({"jobSeeker", "job"})
    private Application application;

    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewMode mode;

    // Meeting link for ONLINE interviews, address for OFFLINE interviews.
    private String location;

    private String interviewerName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewStatus status;

    @Column(length = 1000)
    private String notes;

    @Column(length = 1000)
    private String feedback;

    private LocalDateTime createdAt;
}
