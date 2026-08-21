package com.aitrich.JobPortalSystem.DTO;

import lombok.Getter;
import lombok.Setter;

// Used for "resend OTP" - only needs the email
@Getter
@Setter
public class OtpEmailDTO {
    private String email;
}
