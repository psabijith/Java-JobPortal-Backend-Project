package com.aitrich.JobPortalSystem.Service.Email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void send(String to, String subject, String body) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);
            mailSender.send(msg);
        } catch (Exception e) {
            System.err.println("Email failed: " + e.getMessage());
        }
    }

    // Call this after saving a new JobSeeker or Company
    public void sendWelcome(String to, String name) {
        send(to, "Welcome to JobPortal!",
                "Hi " + name + ", your account is ready.");
    }

    // Call this after POST /api/applications
    public void sendApplicationAlert(String companyEmail, String applicant, String job) {
        send(companyEmail,
                "New application: " + job,
                applicant + " has applied to " + job);
    }

    // Call this after PUT /api/applications/{id}/{status}
    public void sendStatusUpdate(String seekerEmail, String job, String status) {
        send(seekerEmail,
                "Application update: " + job,
                "Your application for " + job
                        + " is now: " + status);
    }

    // Call this for password reset
    public void sendPasswordReset(String to, String link) {
        send(to, "Reset your password",
                "Click here to reset: " + link);
    }

    // ---- New methods below (added for Interview module + OTP registration) ----
    // Nothing above this line was changed.

    // Call this after generating an OTP for the two-step registration flow
    public void sendOtpEmail(String to, String otp, int validMinutes) {
        send(to, "Your JobPortal verification code",
                "Your OTP is: " + otp + ". It is valid for " + validMinutes + " minutes. "
                        + "Do not share this code with anyone.");
    }

    // Call this when a company schedules/reschedules an interview
    public void sendInterviewScheduled(String to, String candidateName, String jobTitle,
                                        String scheduledAt, String mode, String location) {
        send(to, "Interview scheduled: " + jobTitle,
                "Hi " + candidateName + ", your interview for " + jobTitle
                        + " has been scheduled on " + scheduledAt + " (" + mode + "). "
                        + (location != null ? "Details: " + location : ""));
    }

    // Call this when an interview's status changes (completed/cancelled/etc.)
    public void sendInterviewStatusUpdate(String to, String jobTitle, String status) {
        send(to, "Interview update: " + jobTitle,
                "Your interview for " + jobTitle + " is now: " + status);
    }
}