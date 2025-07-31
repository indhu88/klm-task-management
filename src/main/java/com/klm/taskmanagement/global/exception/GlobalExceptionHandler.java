/**
 * This package contains custom exception classes and handlers
 * for managing error scenarios within the user module of
 * the task management system.
 */
package com.klm.taskmanagement.global.exception;

import com.klm.taskmanagement.global.AppConstants;
import com.klm.taskmanagement.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the user module.
 * <p>
 * This class catches and handles exceptions thrown by REST controllers
 * and returns standardized API responses with appropriate HTTP status codes.
 * <p>
 * It handles:
 * - RuntimeException (generic bad requests),
 * - ResourceNotFoundException (returns 404),
 * - MethodArgumentNotValidException (validation errors),
 * - and generic Exception (returns 500 internal errors).
 * <p>
 * The @Hidden annotation excludes this class from Swagger/OpenAPI documentation.
 * The @RestControllerAdvice enables centralized exception handling across controllers.
 */
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles ResourceNotFoundException when a requested resource is not found.
     * Returns HTTP 404 Not Found with a descriptive message.
     *
     * @param exception The resource not found exception.
     * @return ResponseEntity with error details.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.error(HttpStatus.NOT_FOUND.value(), exception.getMessage(), null)
        );
    }

    /**
     * Handles validation exceptions thrown when method arguments fail validation.
     * Collects all field errors and returns HTTP 400 Bad Request with details.
     *
     * @param ex The MethodArgumentNotValidException exception.
     * @return ResponseEntity with validation error messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(
                ApiResponse.error(400, "Validation failed", errors)
        );
    }

    /**
     * Handles all other exceptions not specifically handled by other methods.
     *
     * Important:
     * - If the exception is a Spring Security authorization-related exception
     *   (AccessDeniedException or AuthorizationDeniedException), it is rethrown.
     * - For all other exceptions, we log the error and return a generic
     *   500 Internal Server Error response with a standard error message.
     *
     * @param ex The caught exception.
     * @return ResponseEntity with error details or rethrows Spring Security exceptions.
     * @throws Exception when a Spring Security authorization exception is detected.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception ex) throws Exception {
        // If exception is Spring Security authorization related, rethrow it so Spring Security can handle
        if (ex instanceof AccessDeniedException || ex instanceof AuthorizationDeniedException) {
            throw ex;
        }
        logger.error("Unhandled exception caught in GlobalExceptionHandler: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.error(500, "Internal Server Error", null)
        );
    }
    /**
     * Handles {@link ConflictException} thrown when a version conflict occurs
     * during optimistic locking updates (e.g., a task was modified by another user).
     * <p>
     * Returns a structured {@link ApiResponse} with HTTP 409 status and a descriptive message.
     *
     * @param ex the ConflictException containing the error message
     * @return a ResponseEntity with {@link ApiResponse} wrapping the conflict error details
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflict(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(409,ex.getMessage(),null));
    }
    /**
     * Handles {@link BadCredentialsException} thrown during authentication failures.
     * <p>
     * This method catches login attempts with incorrect username or password and returns
     * a consistent error response with a 401 Unauthorized status code and a user-friendly message.
     *
     * @param ex the exception thrown when authentication fails due to bad credentials
     * @return a structured {@link ResponseEntity} containing status, message, null data, and timestamp
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401,AppConstants.INVALID_CREDENTIALS,null));
    }

}