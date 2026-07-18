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
}