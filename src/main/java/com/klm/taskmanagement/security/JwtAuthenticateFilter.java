/**
 * This package contains security-related classes for JWT authentication.
 */
package com.klm.taskmanagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication filter that intercepts HTTP requests once per request.
 * <p>
 * It extracts the JWT token from the Authorization header, validates it,
 * retrieves user details, and sets the authentication in the security context
 * if the token is valid.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticateFilter extends OncePerRequestFilter {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final UserInfoService userInfoService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        // Check if Authorization header is present and starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extract JWT token from the header
            String token = authHeader.substring(7);
            if (jwtTokenGenerator.validateToken(token)) {
                // Extract username from the token
                String username = jwtTokenGenerator.getUsernameFromToken(token);
                // Load user details from username
                UserDetails userDetails = userInfoService.loadUserByUsername(username);
                // Create authentication token for Spring Security context
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                // Set request details
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Set authentication in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }


}
