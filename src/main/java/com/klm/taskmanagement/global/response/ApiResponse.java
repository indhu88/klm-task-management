/**
 * This package contains classes related to
 * standardized API responses for the user module
 * of the task management system.
 */
package com.klm.taskmanagement.global.response;

import java.time.Instant;
/**
 * Generic API response wrapper.
 *
 * This record encapsulates the structure of all API responses
 * sent by the backend, including status codes, messages,
 * response data, and timestamps.
 *
 * @param <T> The type of the response data.
 */
public record ApiResponse<T>(
        int status,       // HTTP status code (e.g., 200, 201, 400)
        String message,   // Message describing the response or error
        T data,           // The actual payload
        Instant timestamp // Timestamp when the response was created
) {
    /**
     * Creates a success ApiResponse with status 200 (OK).
     *
     * @param message Success message.
     * @param data    Response data.
     * @param <T>     Type of data.
     * @return ApiResponse with HTTP 200 status.
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data, Instant.now());
    }
    /**
     * Creates an ApiResponse for resource creation with status 201 (Created).
     *
     * @param message Creation success message.
     * @param data    Response data.
     * @param <T>     Type of data.
     * @return ApiResponse with HTTP 201 status.
     */
    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(201, message, data, Instant.now());
    }
    /**
     * Creates an error ApiResponse with a custom HTTP status.
     *
     * @param status  HTTP status code representing the error.
     * @param message Error message.
     * @param data    Additional error details or null.
     * @param <T>     Type of data.
     * @return ApiResponse with specified error status.
     */
    public static <T> ApiResponse<T> error(int status, String message, T data) {
        return new ApiResponse<>(status, message, data, Instant.now());
    }
}