package com.aitrich.JobPortalSystem.Service.Otp;

import com.aitrich.JobPortalSystem.DTO.LoginResponseDTO;
import com.aitrich.JobPortalSystem.DTO.OtpVerifyDTO;
import com.aitrich.JobPortalSystem.DTO.RegisterInitiateDTO;
import com.aitrich.JobPortalSystem.Response.ApiResponse;

public interface IOtpService {

    // Step 1: validate password/confirmPassword, store pending registration, email OTP
    ApiResponse initiateRegistration(RegisterInitiateDTO dto);

    // Step 2: verify OTP, create the real account, and log the user in
    LoginResponseDTO verifyOtpAndRegister(OtpVerifyDTO dto);

    ApiResponse resendOtp(String email);
}
