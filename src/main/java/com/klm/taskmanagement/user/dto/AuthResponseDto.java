/**
 * Contains Data Transfer Objects (DTOs) related to user authentication
 * and user information within the task management system.
 *
 * These DTOs are used to transfer data between client and server,
 * especially during authentication and authorization processes.
 */
package com.klm.taskmanagement.user.dto;
import java.util.Set;
/**
 * Represents the response returned after a user successfully authenticates.
 * This DTO carries the authentication token and basic user information.
 *
 * @param token    The authentication token (e.g., JWT) issued to the user.
 * @param userName The username of the authenticated user.
 * @param email    The email address of the authenticated user.
 * @param roles    The set of roles (permissions) assigned to the user.
 */
public record AuthResponseDto(String token, String userName,String email, Set<String> roles) {
}
