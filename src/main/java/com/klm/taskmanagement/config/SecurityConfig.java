// Package for Spring Security configuration specific to the user module
package com.klm.taskmanagement.config;

import com.klm.taskmanagement.security.CustomAccessDeniedHandler;
import com.klm.taskmanagement.security.JwtAuthenticateFilter;
import com.klm.taskmanagement.security.UnauthorizedAccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Main Spring Security configuration class.
 * SecurityConfig defines the security filter chain, authentication manager,
 * and password encoder for the Task Management application.
 * - Configures JWT-based stateless authentication
 * - Permits access to public endpoints like /auth, Swagger, and H2 console
 * - Secures all other endpoints
 */
@Configuration
@EnableMethodSecurity  // Enables method-level security with @PreAuthorize, @Secured, etc.
@RequiredArgsConstructor  // Injects final fields via constructor (jwtFilter)
public class SecurityConfig {
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtAuthenticateFilter jwtFilter;
   private final UnauthorizedAccessHandler jwtAuthenticationEntryPoint;
    /**
     * Configures the HTTP security filter chain.
     *
     * - Disables CSRF (useful for APIs and H2 console)
     * - Disables frame options to allow H2 console UI
     * - Sets session to stateless (JWT-based authentication)
     * - Permits public endpoints (auth, Swagger, H2)
     * - Secures all other requests
     * - Adds custom JWT filter before Spring’s UsernamePasswordAuthenticationFilter
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


            http
                    .csrf(csrf -> csrf.disable())  // Disable CSRF (not needed for token-based auth)
                    .headers(headers -> headers.frameOptions(frame -> frame.disable())) // Allow H2 console in iframe
                    .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No session; use JWT
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(
                                    "/h2-console/**",
                                    "/api/tasks/**",
                                    "/api/auth/**",
                                    "/v3/api-docs/**",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html",
                                    "/swagger-resources/**",
                                    "/configuration/**",
                                    "/webjars/**",
                                    "/test",
                                    "/ws/**",
                                    "/"  // home or root
                            ).permitAll()  // Publicly accessible

                            .anyRequest().authenticated()  // All other endpoints require authentication
                    )
                    .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)   // Handles 401 Unauthorized
                    .accessDeniedHandler(customAccessDeniedHandler))        // Handles 403 Forbidden
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }

    /**
     * Provides the AuthenticationManager bean required for login authentication.
     *
     * Spring Boot automatically wires the AuthenticationConfiguration
     * based on UserDetailsService and PasswordEncoder.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Provides a BCrypt password encoder for hashing user passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}