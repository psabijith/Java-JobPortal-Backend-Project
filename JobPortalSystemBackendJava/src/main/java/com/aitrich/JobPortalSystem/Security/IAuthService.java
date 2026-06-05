package com.aitrich.JobPortalSystem.Security;

import com.aitrich.JobPortalSystem.DTO.LoginRequestDTO;

public interface IAuthService {
    String login(LoginRequestDTO dto);
}
