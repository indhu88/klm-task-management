package com.klm.taskmanagement.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom implementation of {@link AccessDeniedHandler} to handle 403 Forbidden errors.
 * This handler is invoked when an authenticated user tries to access a resource
 * they are not authorized to access (e.g., lacking required roles/permissions).
 * It returns a JSON response with HTTP status 403 and a descriptive message.
 * This helps ensure consistent error responses across the API.

 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * Handles 403 Forbidden errors by returning a structured JSON response.
     *
     * @param request               the current HTTP request
     * @param response              the HTTP response to send to the client
     * @param accessDeniedException the exception thrown when access is denied
     * @throws IOException if writing to the response fails
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write("{\"status\":403,\"message\":\"Access denied: You do not have permission.\"}");
    }
}