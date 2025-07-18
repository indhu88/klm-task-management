/**
 * This package contains the implementation of
 * user-related business logic for the task management system.
 */
package com.klm.taskmanagement.user.service;

import com.klm.taskmanagement.global.AppConstants;
import com.klm.taskmanagement.global.exception.ConflictException;
import com.klm.taskmanagement.global.exception.ResourceNotFoundException;
import com.klm.taskmanagement.security.JwtTokenGenerator;
import com.klm.taskmanagement.security.UserInfoDetails;
import com.klm.taskmanagement.user.dto.AuthResponseDto;
import com.klm.taskmanagement.user.dto.LoginDto;
import com.klm.taskmanagement.user.dto.RegisterDto;
import com.klm.taskmanagement.user.dto.UserResponse;
import com.klm.taskmanagement.user.entity.Role;
import com.klm.taskmanagement.user.entity.User;
import com.klm.taskmanagement.user.mapper.UserMapper;
import com.klm.taskmanagement.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation that handles all user-related operations,
 * including registration, login, retrieval, update, deletion,
 * and role management.
 * <p>
 * Uses Spring Security for authentication and password encoding,
 * and ModelMapper for mapping between entities and DTOs.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    // Spring Data JPA repository for User entity operations
    private final UserRepository userRepository;
    // Password encoder to securely hash user passwords
    private final PasswordEncoder passwordEncoder;
    // JWT token generator for authentication tokens
    private final JwtTokenGenerator jwtTokenGenerator;
    // Spring Security authentication manager for login authentication
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    /**
     * Registers a new user if username/email are not taken,
     * encrypts password, assigns USER role,
     * saves the user and returns authentication response.
     */
    @Override
    public AuthResponseDto register(RegisterDto request) {
        if (userRepository.existsByUsername(request.userName()) || userRepository.existsByEmail(request.email())) {
            throw new ConflictException(AppConstants.USERNAME_EMAIL_EXISTS);
        }

        User user = User.builder()
                .username(request.userName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(Role.ROLE_ADMIN))
                .build();
        userRepository.save(user);
        String token = jwtTokenGenerator.generateToken(user.getUsername(), getRoles(user.getRoles()));
        return new AuthResponseDto(token, user.getUsername(), user.getEmail(), getRoles(user.getRoles()));
    }

    /**
     * Authenticates user credentials and returns
     * AuthResponseDto with token and user details.
     */
    @Override
    public AuthResponseDto login(LoginDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(), request.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserInfoDetails userDetails = (UserInfoDetails) authentication.getPrincipal();
        User user = userDetails.getUser();  // <-- get the actual User entity from your UserInfoDetails
        Set<String> roleNames = getRoles(user.getRoles());
        String token = jwtTokenGenerator.generateToken(
                user.getUsername(),
                roleNames
        );
        return new AuthResponseDto(token, user.getUsername(), user.getEmail(), getRoles(user.getRoles()));
    }

    /**
     * Deletes the user by ID if exists, otherwise throws RuntimeException.
     */
    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));
        if (isNotAdminRole(user)) {
            throw new AccessDeniedException(AppConstants.ROLE_USER_DELETE);
        }
        userRepository.delete(user);
    }

    /**
     * Fetches user by ID and maps to UserResponse DTO.
     * Throws ResourceNotFoundException if user not found.
     */
    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    /**
     * Fetches user by ID and maps to UserResponse DTO.
     * Throws ResourceNotFoundException if user not found.
     */
    @Override
    public UserResponse updateUser(Long id, RegisterDto updateUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));
        user.setUsername(updateUser.userName());
        user.setEmail(updateUser.email());
        userRepository.save(user);
        return userMapper.toUserResponse(user);

    }

    /**
     * Retrieves all users from repository and maps to UserResponse DTO list.
     */
    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::toUserResponse).toList();
    }

    /**
     * Updates the roles assigned to a user and returns updated user info.
     * Throws RuntimeException if user not found.
     */
    @Override
    public UserResponse updateUserRoles(Long userId, Set<Role> roles) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));
        if (isNotAdminRole(user)) {
            throw new AccessDeniedException(AppConstants.ACCESS_DENIED);
        }
        user.setRoles(roles);
        userRepository.save(user);
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRoles());
    }

    /**
     * Helper method to map User entity to UserResponse DTO.
     */
    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRoles());
    }

    private boolean isNotAdminRole(User user) {
        // Prevent users with only ROLE_USER from updating roles
        Set<Role> roles = user.getRoles();
        if(roles == null || (roles.size() == 1 && roles.contains(Role.ROLE_USER))) {
            return true;
        }
        return false;
    }

    /**
     * Converts a set of Role enums to their string names.
     */
    private Set<String> getRoles(Set<Role> user) {
        return user.stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

    }
}
