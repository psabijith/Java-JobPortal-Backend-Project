package com.aitrich.JobPortalSystem.Service.Otp;

import com.aitrich.JobPortalSystem.DTO.*;
import com.aitrich.JobPortalSystem.Entity.OtpVerification;
import com.aitrich.JobPortalSystem.Enums.Role;
import com.aitrich.JobPortalSystem.Repository.IOtpVerificationRepo;
import com.aitrich.JobPortalSystem.Repository.IUserRepo;
import com.aitrich.JobPortalSystem.Response.ApiResponse;
import com.aitrich.JobPortalSystem.Security.IAuthService;
import com.aitrich.JobPortalSystem.Service.Company.ICompanyService;
import com.aitrich.JobPortalSystem.Service.Email.EmailService;
import com.aitrich.JobPortalSystem.Service.JobSeeker.IJobSeekerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpServiceImp implements IOtpService {

    private final IOtpVerificationRepo otpRepo;
    private final IUserRepo userRepo;
    private final EmailService emailService;

    // Reusing the existing, untouched services to actually create accounts
    // and log the user in once the OTP is confirmed.
    private final IJobSeekerService jobSeekerService;
    private final ICompanyService companyService;
    private final IAuthService authService;

    private static final int OTP_VALID_MINUTES = 10;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public ApiResponse initiateRegistration(RegisterInitiateDTO dto) {

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new RuntimeException("Email is required");
        }
        if (dto.getRole() == null || dto.getRole() == Role.ADMIN) {
            throw new RuntimeException("Role must be JOBSEEKER or COMPANY");
        }
        if (dto.getPassword() == null || dto.getPassword().length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters");
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Password and Confirm Password do not match");
        }
        if (userRepo.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("An account with this email already exists");
        }

        OtpVerification pending = otpRepo.findByEmail(dto.getEmail()).orElseGet(OtpVerification::new);

        pending.setEmail(dto.getEmail());
        pending.setPassword(dto.getPassword());
        pending.setRole(dto.getRole());
        pending.setFirstName(dto.getFirstName());
        pending.setLastName(dto.getLastName());
        pending.setLocation(dto.getLocation());
        pending.setSkills(dto.getSkills() != null ? dto.getSkills() : new java.util.ArrayList<>());
        pending.setCompanyName(dto.getCompanyName());
        pending.setWebsite(dto.getWebsite());
        pending.setDescription(dto.getDescription());
        pending.setIndustry(dto.getIndustry());
        pending.setEmployeeCount(dto.getEmployeeCount());
        pending.setVerified(false);

        String otp = generateOtp();
        pending.setOtp(otp);
        pending.setOtpExpiry(LocalDateTime.now().plusMinutes(OTP_VALID_MINUTES));

        otpRepo.save(pending);

        emailService.sendOtpEmail(dto.getEmail(), otp, OTP_VALID_MINUTES);

        return ApiResponse.success("OTP sent to " + dto.getEmail() + ". Please verify to complete registration.", null);
    }

    @Override
    public LoginResponseDTO verifyOtpAndRegister(OtpVerifyDTO dto) {

        OtpVerification pending = otpRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("No pending registration found for this email"));

        if (pending.getOtpExpiry().isBefore(LocalDateTime.now())) {
            otpRepo.delete(pending);
            throw new RuntimeException("OTP has expired. Please register again");
        }

        if (!pending.getOtp().equals(dto.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        // Double-check the account still doesn't exist (e.g. no duplicate submits)
        if (userRepo.findByEmail(pending.getEmail()).isPresent()) {
            otpRepo.delete(pending);
            throw new RuntimeException("An account with this email already exists");
        }

        if (pending.getRole() == Role.JOBSEEKER) {
            JobSeekerRequestDTO js = new JobSeekerRequestDTO();
            js.setFirstName(pending.getFirstName());
            js.setLastName(pending.getLastName());
            js.setEmail(pending.getEmail());
            js.setPassword(pending.getPassword());
            js.setSkills(pending.getSkills());
            js.setLocation(pending.getLocation());
            jobSeekerService.createJobSeeker(js);
        } else {
            CompanyRequestDTO company = new CompanyRequestDTO();
            company.setCompanyName(pending.getCompanyName());
            company.setEmail(pending.getEmail());
            company.setPassword(pending.getPassword());
            company.setWebsite(pending.getWebsite());
            company.setLocation(pending.getLocation());
            company.setDescription(pending.getDescription() != null ? pending.getDescription() : "");
            company.setIndustry(pending.getIndustry());
            company.setEmployeeCount(pending.getEmployeeCount());
            companyService.createCompany(company);
        }

        otpRepo.delete(pending);

        // Log the newly created user in straight away, reusing the existing login logic
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail(pending.getEmail());
        loginRequest.setPassword(pending.getPassword());
        return authService.login(loginRequest);
    }

    @Override
    public ApiResponse resendOtp(String email) {
        OtpVerification pending = otpRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No pending registration found for this email"));

        String otp = generateOtp();
        pending.setOtp(otp);
        pending.setOtpExpiry(LocalDateTime.now().plusMinutes(OTP_VALID_MINUTES));
        otpRepo.save(pending);

        emailService.sendOtpEmail(email, otp, OTP_VALID_MINUTES);

        return ApiResponse.success("OTP resent to " + email, null);
    }

    private String generateOtp() {
        int otp = 100000 + RANDOM.nextInt(900000); // 6-digit number
        return String.valueOf(otp);
    }
}
