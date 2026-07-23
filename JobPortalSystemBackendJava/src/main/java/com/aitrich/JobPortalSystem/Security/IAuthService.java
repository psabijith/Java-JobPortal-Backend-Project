package com.aitrich.JobPortalSystem.Security;

import com.aitrich.JobPortalSystem.DTO.LoginRequestDTO;
import com.aitrich.JobPortalSystem.DTO.LoginResponseDTO;

public interface IAuthService {
    LoginResponseDTO login(LoginRequestDTO dto);
}
