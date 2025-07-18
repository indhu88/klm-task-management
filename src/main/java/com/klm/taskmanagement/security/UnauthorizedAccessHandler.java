package com.klm.taskmanagement.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/** Marks this class as a Spring component so that Spring can detect and manage it as a bean
 */
@Component
public class UnauthorizedAccessHandler implements AuthenticationEntryPoint {
    /**
     * This method is called whenever an unauthenticated user tries to access
     * a protected resource without a valid JWT token.
     *
     * @param request          the HTTP request
     * @param response         the HTTP response
     * @param authException    the exception thrown when authentication fails
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        // Step 1: Set the HTTP status code to 401 (Unauthorized)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // Step 2: Set the response content type to application/json
        response.setContentType("application/json");
        // Step 3: Write a JSON error message to the response body
        response.getWriter().write("{\"error\": \"Missing or invalid token. Please login or provide a valid Authorization header.\"}");
    }
}