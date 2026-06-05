package com.aitrich.JobPortalSystem.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "job_seekers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobSeeker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String resumeUrl;
    private String photoUrl;

    @ElementCollection
    private List<String> skills = new ArrayList<>();

    private String location;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "jobSeeker", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Application> applications = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "job_seeker_saved_jobs",
        joinColumns = @JoinColumn(name = "job_seeker_id"),
        inverseJoinColumns = @JoinColumn(name = "job_id")
    )
    private List<Job> savedJobs = new ArrayList<>();
}
