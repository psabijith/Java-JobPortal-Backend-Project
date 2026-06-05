package com.aitrich.JobPortalSystem.Configuration;

import com.aitrich.JobPortalSystem.Security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                // Public endpoints - anyone can register or browse jobs
                .requestMatchers(HttpMethod.POST, "/api/jobseekers").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/company").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/jobs").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/jobs/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/jobs/search").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/jobs/active").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/jobs/location/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/jobs/recent").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/jobs/title").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/jobs/type/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/company").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/company/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/company/active").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/company/search").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/company/top-hiring").permitAll()

                // Admin-only endpoints
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // Job management - COMPANY or ADMIN
                .requestMatchers(HttpMethod.POST, "/api/jobs").hasAnyRole("COMPANY", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/jobs/**").hasAnyRole("COMPANY", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/jobs/**").hasAnyRole("COMPANY", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/jobs/**").hasAnyRole("COMPANY", "ADMIN")

                // JobSeeker actions - JOBSEEKER or ADMIN
                .requestMatchers(HttpMethod.GET, "/api/jobseekers/**").hasAnyRole("JOBSEEKER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/jobseekers/**").hasAnyRole("JOBSEEKER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/jobseekers/**").hasAnyRole("JOBSEEKER", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/jobseekers/**").hasAnyRole("JOBSEEKER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/jobseekers/**").hasAnyRole("JOBSEEKER", "ADMIN")

                // Applications - authenticated users
                .requestMatchers("/api/applications/**").authenticated()

                // Company management (update/delete) - COMPANY or ADMIN
                .requestMatchers(HttpMethod.PUT, "/api/company/**").hasAnyRole("COMPANY", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/company/**").hasAnyRole("COMPANY", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/company/**").hasAnyRole("COMPANY", "ADMIN")

                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }
}
