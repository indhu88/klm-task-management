/**
 * This package contains controller classes for user authentication and management,
 * including login, registration, role updates, and basic CRUD operations.
 */
package com.klm.taskmanagement.user.controller;

import com.klm.taskmanagement.global.AppConstants;
import com.klm.taskmanagement.global.response.ApiResponse;
import com.klm.taskmanagement.user.dto.AuthResponseDto;
import com.klm.taskmanagement.user.dto.LoginDto;
import com.klm.taskmanagement.user.dto.RegisterDto;
import com.klm.taskmanagement.user.dto.UserResponse;
import com.klm.taskmanagement.user.entity.Role;
import com.klm.taskmanagement.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
/**
 * REST controller for handling user authentication and management.
 * Exposes endpoints for:
 * - User registration and login
 * - Updating roles (ADMIN only)
 * - Fetching, updating, and deleting users
 * All routes are prefixed with <code>/api/auth</code>.
 *
 */
@RestController
@RequestMapping("/api/auth")// All routes will be prefixed with /api/auth
@RequiredArgsConstructor
public class UserController {
    // Inject user service implementation
    private final UserServiceImpl userService;
    // Logger for debugging / tracking
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    /**
     * Registers a new user with the system.
     *
     * @param request the registration request data
     * @return response containing JWT token and user info
     */
    @PostMapping("/register")
    public ApiResponse<AuthResponseDto> register(@RequestBody @Valid RegisterDto request) {
        AuthResponseDto response = userService.register(request);
        return ApiResponse.created(AppConstants.USER_REGISTER, response);
    }
    /**
     * Authenticates a user and issues a JWT token.
     *
     * @param request the login request containing username and password
     * @return response with JWT token and authenticated user information
     */
    @PostMapping("/login")
    public ApiResponse<AuthResponseDto> login(
            @RequestBody @Valid LoginDto request) {
        AuthResponseDto response = userService.login(request);

        return ApiResponse.success(AppConstants.USER_LOGIN, response);
    }
    /**
     * Updates the roles assigned to a user. Only accessible to ADMIN users.
     *
     * @param id    the user ID whose roles need to be updated
     * @param roles the new set of roles
     * @return response with updated user information
     */
    @PatchMapping("/{id}/user-role-update")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> updateUserRoles(@PathVariable Long id, @RequestBody @Valid Set<Role> roles) {
        UserResponse response = userService.updateUserRoles(id, roles);
        return ApiResponse.success(AppConstants.ROLE_UPDATE, response);
        //return userService.updateUserRoles(id, request);
    }
    /**
     * Retrieves a list of all users in the system.
     * Only accessible to ADMIN users.
     *
     * @return response with a list of all registered users
     */
    @GetMapping("/all-users")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ApiResponse.success(AppConstants.USER_LIST, users);
    }
    /**
     * Retrieves a specific user by ID.
     *
     * @param id the ID of the user
     * @return response with user details
     */
    @GetMapping("/{id}/user-info")
    @PreAuthorize("isAuthenticated()")  // Require any authenticated user (token)
    public ApiResponse<UserResponse> getUser(@PathVariable Long id) {
        UserResponse response = userService.getUserById(id);
        return ApiResponse.success(AppConstants.USER_LIST, response);
    }
    /**
     * Updates basic information (e.g. name, email) of a user.
     * Only accessible to ADMIN users.
     *
     * @param id      the ID of the user to update
     * @param request the new user details
     * @return response with updated user info
     */
    @PutMapping("/{id}/user-update")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ApiResponse<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody RegisterDto request) {
        UserResponse response = userService.updateUser(id, request);
        return ApiResponse.success(AppConstants.USER_UPDATE, response);

    }
    /**
     * Deletes a user from the system by ID.
     * Only accessible to ADMIN users.
     *
     * @param id the ID of the user to delete
     * @return a success response with no content
     */
    @DeleteMapping("/{id}/user-delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(AppConstants.USER_DELETE, null));
    }
}
