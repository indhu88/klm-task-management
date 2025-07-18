/**
 * Contains Data Transfer Objects (DTOs) related to user Login within the task management system.
 *
 * These DTOs are used to transfer data between client and server,
 * especially during authentication and authorization processes.
 */
package com.klm.taskmanagement.user.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for user login credentials.
 *
 * This DTO is used to capture the username and password
 * submitted during the login process.
 *
 * Both fields are mandatory and validated to ensure they are not blank.
 *
 * @param username The username of the user attempting to log in.
 *                 Cannot be null or empty.
 * @param password The password of the user attempting to log in.
 *                 Cannot be null or empty.
 */
public record LoginDto(@NotBlank(message="Username is required") String username, @NotBlank(message = "Password is required") String password) {
}
