package com.aitrich.JobPortalSystem.Service.Authentication;

import com.aitrich.JobPortalSystem.DTO.LoginRequestDTO;
import com.aitrich.JobPortalSystem.Entity.User;
import com.aitrich.JobPortalSystem.Repository.IUserRepo;
import com.aitrich.JobPortalSystem.Security.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements IAuthService {

    private final IUserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(LoginRequestDTO dto) {
        // Use a single generic message for both "not found" and "wrong password"
        // to prevent email enumeration attacks.
        Optional<User> userOpt = userRepo.findByEmail(dto.getEmail());

        if (userOpt.isEmpty() || !passwordEncoder.matches(dto.getPassword(), userOpt.get().getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userOpt.get();
        return "Login successful. Role: " + user.getRole();
    }
}
