package com.aitrich.JobPortalSystem.Service.Authentication;

import com.aitrich.JobPortalSystem.DTO.LoginRequestDTO;
import com.aitrich.JobPortalSystem.Entity.User;
import com.aitrich.JobPortalSystem.Repository.IUserRepo;
import com.aitrich.JobPortalSystem.Security.CustomUserDetailsService;
import com.aitrich.JobPortalSystem.Security.IAuthService;
import com.aitrich.JobPortalSystem.Security.jwt.jwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements IAuthService {

    private final IUserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final jwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public String login(LoginRequestDTO dto) {

        Optional<User> userOpt = userRepo.findByEmail(dto.getEmail());

        if (userOpt.isEmpty() ||
                !passwordEncoder.matches(dto.getPassword(), userOpt.get().getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userOpt.get();

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(user.getEmail());

        String token = jwtUtil.generateToken(userDetails);

        return "Login successful.\n"
                + "Role: " + user.getRole() + "\n"
                + "Token: " + token;
    }
}