package com.aitrich.JobPortalSystem.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Job> jobs;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String website;

    private String location;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private boolean active = true;

    private String industry;

    private Integer employeeCount;
}
