/**
 * This package contains Data Transfer Objects (DTOs) used for user-related
 * operations in the task management system, including authentication,
 * registration, and login.
 *
 * Each DTO encapsulates data with validation annotations to ensure
 * data integrity before processing.
 */

package com.klm.taskmanagement.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
/**
 * Data Transfer Object for user registration details.
 *
 * This DTO captures the information required to register a new user.
 * Validation annotations ensure that the input data meets basic requirements:
 * - username must not be blank,
 * - password must not be blank and must be at least 6 characters long,
 * - email must not be blank and must have a valid email format.
 *
 * @param userName The username chosen by the user. Cannot be blank.
 * @param password The password chosen by the user. Must be at least 6 characters.
 * @param email    The user's email address. Must be a valid email format.
 */
public record RegisterDto(@NotBlank(message = "Username is required") String userName,
                          @NotBlank(message = "Password is required")
                          @Size(min = 6, message = "Password must be at least 6 characters") String password,
                          @NotBlank(message = "Email is required")
                          @Email(message = "Invalid email format")
                          String email) {
}
