package com.aitrich.JobPortalSystem.DTO;

import com.aitrich.JobPortalSystem.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    String message;
    Role role;
    String token;

}
