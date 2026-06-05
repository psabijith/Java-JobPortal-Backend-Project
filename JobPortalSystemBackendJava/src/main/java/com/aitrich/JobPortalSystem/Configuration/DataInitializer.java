package com.aitrich.JobPortalSystem.Configuration;

import com.aitrich.JobPortalSystem.Entity.Admin;
import com.aitrich.JobPortalSystem.Entity.User;
import com.aitrich.JobPortalSystem.Enums.Role;
import com.aitrich.JobPortalSystem.Repository.IAdminRepo;
import com.aitrich.JobPortalSystem.Repository.IUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final IUserRepo userRepo;
    private final IAdminRepo adminRepo;
    private final PasswordEncoder passwordEncoder;

    // Set ADMIN_PASSWORD in your environment or application.properties
    // e.g.  ADMIN_PASSWORD=<strong-random-value>
    @Value("${admin.default.password:CHANGE_ME_IN_ENV}")
    private String adminDefaultPassword;

    @Override
    public void run(String... args) {
        if (userRepo.findByEmail("admin@jobportal.com").isEmpty()) {
            if ("CHANGE_ME_IN_ENV".equals(adminDefaultPassword)) {
                throw new IllegalStateException(
                    "Admin password is not configured. Set the 'admin.default.password' " +
                    "property or the ADMIN_PASSWORD environment variable before starting.");
            }

            User adminUser = new User();
            adminUser.setEmail("admin@jobportal.com");
            adminUser.setPassword(passwordEncoder.encode(adminDefaultPassword));
            adminUser.setRole(Role.ADMIN);
            userRepo.save(adminUser);

            Admin admin = new Admin();
            admin.setName("System Admin");
            admin.setEmail("admin@jobportal.com");
            admin.setPassword(adminUser.getPassword());
            admin.setRole("ADMIN");
            adminRepo.save(admin);

            // Do NOT log the password — check your server logs after first run
            System.out.println("Default admin account created: admin@jobportal.com");
        }
    }
}
