package com.klm.taskmanagement.user.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link UserServiceImpl}.
 * This test class uses Mockito to mock dependencies and test user registration and login functionality.
 */
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenGenerator jwtTokenGenerator;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserMapper userMapper;
    /**
     * Initializes mocks before each test using MockitoAnnotations.
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }
    /**
     * Tests that a new user is successfully registered when provided valid input.
     * Verifies token generation and repository interaction.
     */

    @Test
    void register_ShouldRegisterUser_WhenValidInput() {
        RegisterDto registerDto = new RegisterDto("testuser", "test@example.com", "password");
        User user = User.builder()
                .username(registerDto.userName())
                .email(registerDto.email())
                .password("encodedPassword")
                .roles(Set.of(Role.ROLE_ADMIN))
                .build();

        when(userRepository.existsByUsername(registerDto.userName())).thenReturn(false);
        when(userRepository.existsByEmail(registerDto.email())).thenReturn(false);
        when(passwordEncoder.encode(registerDto.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtTokenGenerator.generateToken(anyString(), anySet())).thenReturn("jwt-token");
        when(userMapper.toAuthResponse(any(User.class))).thenReturn(
                new AuthResponseDto("jwt-token", user.getUsername(), user.getEmail(), Set.of("USER"))
        );

        AuthResponseDto response = userService.register(registerDto);

        assertNotNull(response);
        assertEquals("jwt-token", response.token());
        assertEquals("testuser", response.userName());
        verify(userRepository, times(1)).save(any(User.class));
    }
    /**
     * Tests that registration throws an exception when the username already exists.
     */
    @Test
    void register_ShouldThrowException_WhenUsernameExists() {
        RegisterDto registerDto = new RegisterDto("testuser", "test@example.com", "password");

        when(userRepository.existsByUsername(registerDto.userName())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.register(registerDto));
        assertEquals("Username or Email already exists", exception.getMessage());
    }
    /**
     * Tests that fetching a user by ID returns a valid UserResponse when the user exists.
     */
    @Test
    void getUserById_ShouldReturnUserResponse_WhenUserExists() {
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .username("testuser")
                .email("test@example.com")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        UserResponse userResponse = new UserResponse(userId, "testuser", "test@example.com", Set.of());
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.id());
        assertEquals("testuser", result.username());
    }
    /**
     * Tests that fetching a user by ID throws ResourceNotFoundException when the user does not exist.
     */
    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    }
    @Test
    void testLogin_Success() {
        // Given
        LoginDto loginDto = new LoginDto("testuser", "password123");

        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("encodedPassword")
                .roles(Set.of(Role.ROLE_USER))
                .build();

        UserInfoDetails userDetails = new UserInfoDetails(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        when(authenticationManager.authenticate(any()))
                .thenReturn(auth);

        when(jwtTokenGenerator.generateToken(eq(user.getUsername()), anySet()))
                .thenReturn("mocked-jwt-token");

        // When
        AuthResponseDto response = userService.login(loginDto);

        // Then
        assertEquals("mocked-jwt-token", response.token());
        assertEquals("testuser", response.userName());
        assertEquals("test@example.com", response.email());
        assertTrue(response.roles().contains("ROLE_USER"));
    }
    /**
     * Test successful retrieval of all users returns a non-empty list.
     */
    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        User user = User.builder().id(1L).username("testuser").email("test@example.com").build();
        when(userRepository.findAll()).thenReturn(List.of(user));
        UserResponse response = new UserResponse(1L, "testuser", "test@example.com", Set.of());
        when(userMapper.toUserResponse(any(User.class))).thenReturn(response);

        List<UserResponse> result = userService.getAllUsers();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("testuser", result.get(0).username());
        verify(userRepository, times(1)).findAll();
    }

    /**
     * Failure case: getAllUsers should return empty list when no users exist.
     */
    @Test
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsers() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<UserResponse> result = userService.getAllUsers();


        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Test successful user update when user exists.
     */
    @Test
    void updateUser_ShouldUpdateUser_WhenUserExists() {
        Long userId = 1L;
        RegisterDto dto = new RegisterDto("updatedUser", "updated@example.com", "newPassword");

        User existingUser = User.builder().id(userId).username("oldUser").email("old@example.com").build();
        User updatedUser = User.builder().id(userId).username(dto.userName()).email(dto.email()).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        UserResponse userResponse = new UserResponse(userId, dto.userName(), dto.email(), Set.of());
        when(userMapper.toUserResponse(updatedUser)).thenReturn(userResponse);

        UserResponse result = userService.updateUser(userId, dto);

        assertNotNull(result);
        assertEquals(dto.userName(), result.username());
        assertEquals(dto.email(), result.email());
        verify(userRepository).save(any(User.class));
    }

    /**
     * Failure case: updateUser should throw exception when user not found.
     */
    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        Long userId = 1L;
        RegisterDto dto = new RegisterDto("updatedUser", "updated@example.com", "newPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(userId, dto));
    }

    /**
     * Test successful deletion of a user when user exists.
     */
    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        Long userId = 1L;
        User user = User.builder().id(userId).username("testuser").roles(Set.of(Role.ROLE_ADMIN)).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        assertDoesNotThrow(() -> userService.deleteUser(userId));

        verify(userRepository).delete(user);
    }

    /**
     * Failure case: deleteUser should throw exception when user not found.
     */
    @Test
    void deleteUser_ShouldThrowException_WhenUserNotFound() {
        Long userId = 1L;
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRoles(Set.of(Role.ROLE_ADMIN));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(userId));
    }

    /**
     * Test successful update of user roles when user exists.
     */
    @Test
    void updateUserRoles_ShouldUpdateRoles_WhenUserExists() {
        Long userId = 1L;
        Set<Role> newRoles = Set.of(Role.ROLE_ADMIN);

        User user = User.builder().id(userId).roles(Set.of(Role.ROLE_ADMIN)).build();
        User updatedUser = User.builder().id(userId).roles(newRoles).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        UserResponse userResponse = new UserResponse(userId, null, null, newRoles);
        when(userMapper.toUserResponse(updatedUser)).thenReturn(userResponse);

        UserResponse result = userService.updateUserRoles(userId, newRoles);

        assertNotNull(result);
        assertEquals(newRoles, result.roles());
        verify(userRepository).save(user);
    }

    /**
     * Failure case: updateUserRoles should throw exception when user not found.
     */
    @Test
    void updateUserRoles_ShouldThrowException_WhenUserNotFound() {
        Long userId = 1L;
        Set<Role> roles = Set.of(Role.ROLE_ADMIN);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUserRoles(userId, roles));
    }
}