package com.aitrich.JobPortalSystem.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpVerifyDTO {
    private String email;
    private String otp;
}
