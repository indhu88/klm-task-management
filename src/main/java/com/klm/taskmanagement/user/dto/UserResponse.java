/**
 * Contains Data Transfer Objects (DTOs) related to user information
 * and authentication within the task management system.
 * These DTOs are designed to transfer user data between layers,
 * encapsulating user details, authentication responses, login credentials,
 * and registration data.
 */

package com.klm.taskmanagement.user.dto;

import com.klm.taskmanagement.user.entity.Role;

import java.util.Set;
/**
 * Data Transfer Object representing the user details
 * returned from the system, typically after fetching user info.
 *
 * This DTO includes the user’s ID, username, email, and assigned roles.
 *
 * @param id       The unique identifier of the user.
 * @param username The username of the user.
 * @param email    The email address of the user.
 * @param roles    The set of roles assigned to the user.
 */


public record UserResponse(
        Long id,
        String username,
        String email,
        Set<Role> roles
) {


}