package com.aitrich.JobPortalSystem.Entity;

import com.aitrich.JobPortalSystem.Enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Holds a registration that is "pending" until the user confirms the OTP
// sent to their email. Nothing here is a real account yet - the real
// JobSeeker/Company row only gets created once verifyOtp() succeeds.
@Entity
@Table(name = "otp_verifications")
@Getter
@Setter
@NoArgsConstructor
public class OtpVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String otp;

    @Column(nullable = false)
    private LocalDateTime otpExpiry;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Plain password, kept only until OTP is verified, then the row is
    // deleted. The real account stores the BCrypt-encoded password.
    private String password;

    // JobSeeker fields
    private String firstName;
    private String lastName;
    private String location;

    @ElementCollection
    private List<String> skills = new ArrayList<>();

    // Company fields
    private String companyName;
    private String website;
    private String description;
    private String industry;
    private Integer employeeCount;

    private boolean verified = false;
}
