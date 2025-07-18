/**
 * This package contains service interfaces that define
 * business logic and operations related to user management
 * in the task management system.
 */
package com.klm.taskmanagement.user.service;

import com.klm.taskmanagement.user.dto.AuthResponseDto;
import com.klm.taskmanagement.user.dto.LoginDto;
import com.klm.taskmanagement.user.dto.RegisterDto;
import com.klm.taskmanagement.user.dto.UserResponse;
import com.klm.taskmanagement.user.entity.Role;

import java.util.List;
import java.util.Set;

/**
 * Service interface for user-related operations.
 *
 * Defines the contract for user registration, authentication,
 * retrieval, update, deletion, and role management.
 */
public interface UserService {

    /**
     * Registers a new user with the provided registration details.
     *
     * @param registerDto DTO containing username, password, and email.
     * @return AuthResponseDto containing authentication token and user details.
     */
    AuthResponseDto register(RegisterDto registerDto);
    /**
     * Authenticates a user with given login credentials.
     *
     * @param loginDto DTO containing username and password.
     * @return AuthResponseDto containing authentication token and user details.
     */
    AuthResponseDto login(LoginDto loginDto);
    /**
     * Deletes a user by their unique ID.
     *
     * @param id The ID of the user to delete.
     */
    void deleteUser(Long id);
    /**
     * Retrieves user details by user ID.
     *
     * @param id The ID of the user.
     * @return UserResponse DTO containing user information.
     */
    UserResponse getUserById(Long id);

    /**
     * Updates user details for the given user ID.
     *
     * @param id          The ID of the user to update.
     * @param registerDto DTO containing updated username, password, and email.
     * @return Updated UserResponse DTO.
     */
    UserResponse updateUser(Long id, RegisterDto registerDto);
    /**
     * Retrieves a list of all users.
     *
     * @return List of UserResponse DTOs for all users.
     */
    List<UserResponse> getAllUsers();
    /**
     * Retrieves a list of all users.
     *
     * @return List of UserResponse DTOs for all users.
     */
    UserResponse updateUserRoles(Long userId, Set<Role> roles);

}
