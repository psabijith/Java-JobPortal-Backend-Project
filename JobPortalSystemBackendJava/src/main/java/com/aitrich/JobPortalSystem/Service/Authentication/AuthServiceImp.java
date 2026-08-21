package com.aitrich.JobPortalSystem.Service.Authentication;

import com.aitrich.JobPortalSystem.DTO.LoginRequestDTO;
import com.aitrich.JobPortalSystem.DTO.LoginResponseDTO;
import com.aitrich.JobPortalSystem.Entity.Admin;
import com.aitrich.JobPortalSystem.Entity.Company;
import com.aitrich.JobPortalSystem.Entity.JobSeeker;
import com.aitrich.JobPortalSystem.Entity.User;
import com.aitrich.JobPortalSystem.Enums.Role;
import com.aitrich.JobPortalSystem.Repository.IAdminRepo;
import com.aitrich.JobPortalSystem.Repository.ICompanyRepo;
import com.aitrich.JobPortalSystem.Repository.IJobSeekerRepo;
import com.aitrich.JobPortalSystem.Repository.IUserRepo;
import com.aitrich.JobPortalSystem.Security.CustomUserDetailsService;
import com.aitrich.JobPortalSystem.Security.IAuthService;
import com.aitrich.JobPortalSystem.Security.jwt.jwtUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final IJobSeekerRepo  jobSeekerRepo;
    private final ICompanyRepo companyRepo;
    private final IAdminRepo adminRepo;

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {

        Optional<User> userOpt = userRepo.findByEmail(dto.getEmail());

        if (userOpt.isEmpty() ||
                !passwordEncoder.matches(dto.getPassword(), userOpt.get().getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userOpt.get();

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(user.getEmail());

        String token = jwtUtil.generateToken(userDetails);

        LoginResponseDTO response =  new LoginResponseDTO();

        JobSeeker jobSeeker = jobSeekerRepo.findByEmail(user.getEmail());
        Optional<Company> company = companyRepo.findByEmail(user.getEmail());
        Optional<Admin> admin = adminRepo.findByEmail(user.getEmail());

        response.setMessage("Login Successful");
        response.setToken(token);
        response.setRole(user.getRole());
        if(user.getRole() == Role.JOBSEEKER){
            response.setId(jobSeeker.getId());
        } else if (user.getRole() == Role.COMPANY) {
            response.setId(company.get().getId());
        }
        else{
            response.setId(admin.get().getId());
        }


        return response;
    }
}