package com.aitrich.JobPortalSystem.Controller;

import com.aitrich.JobPortalSystem.DTO.LoginResponseDTO;
import com.aitrich.JobPortalSystem.DTO.OtpEmailDTO;
import com.aitrich.JobPortalSystem.DTO.OtpVerifyDTO;
import com.aitrich.JobPortalSystem.DTO.RegisterInitiateDTO;
import com.aitrich.JobPortalSystem.Response.ApiResponse;
import com.aitrich.JobPortalSystem.Service.Otp.IOtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Two-step, OTP-verified registration. Kept separate from the existing
// direct POST /api/jobseekers and POST /api/company endpoints, so nothing
// about the current registration flow needs to change.
@RestController
@RequestMapping("/api/auth/register")
@RequiredArgsConstructor
public class OtpAuthController {

    private final IOtpService otpService;

    // Step 1: submit details + password/confirmPassword -> OTP emailed to user
    @PostMapping("/initiate")
    public ResponseEntity<ApiResponse> initiate(@RequestBody RegisterInitiateDTO dto) {
        return ResponseEntity.ok(otpService.initiateRegistration(dto));
    }

    // Step 2: submit the OTP -> account created + logged in
    @PostMapping("/verify-otp")
    public ResponseEntity<LoginResponseDTO> verifyOtp(@RequestBody OtpVerifyDTO dto) {
        return ResponseEntity.ok(otpService.verifyOtpAndRegister(dto));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse> resendOtp(@RequestBody OtpEmailDTO dto) {
        return ResponseEntity.ok(otpService.resendOtp(dto.getEmail()));
    }
}
