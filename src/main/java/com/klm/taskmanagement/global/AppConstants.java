/**
 * Application-wide constants used throughout the task management system.
 * This includes roles, HTTP header values, error messages, and token configurations.
 */
package com.klm.taskmanagement.global;
/**
 * Contains constant values for roles, headers, error messages, and JWT configuration.
 * Designed as a utility class with a private constructor to prevent instantiation.
 */
public class AppConstants {
    // === Roles ===
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    // === Header ===
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    // ===User Success Messages ===
    public static final String USER_REGISTER = "User registered successfully";
    public static final String USER_LOGIN = "Login successfully";
    public static final String ROLE_UPDATE = "user role has been updated";
    public static final String USER_LIST = "Fetch user successfully";
    public static final String USER_UPDATE = "User updated successfully";
    public static final String USER_DELETE = "User deleted successfully";
    // === Error Messages ===
    public static final String USER_NOT_FOUND = "User not found";
    public static final String NO_USER_NOT_FOUND = "No Users found";
    public static final String NO_TASK_NOT_FOUND = "No Tasks  found";
    public static final String USER_NOT_AVAILABLE= "No user available for assigned user id ";
    public static final String TASK_NOT_FOUND = "Task not found with ID: ";
    public static final String INVALID_CREDENTIALS = "Invalid username or password";
    public static final String EMAIL_EXISTS = "Email already in use";
    public static final String USERNAME_EXISTS = "Username already in use";
    public static final String USERNAME_EMAIL_EXISTS = "Username or Email already exists";
    public static final String ACCESS_DENIED = "ROLE_USER are not allowed to modify.";
    public static final String ROLE_USER_DELETE= "ROLE_USER are not allowed to delete.";
    public static final String CONFLICT_EXCEPTION = "Version mismatch: This task was modified by another user.";
    // ===Task Success Messages ===
    public static final String TASK_CREATED = "Task created successfully";
    public static final String TASK_FETCH = "Fetch task successfully";
    public static final String TASK_UPDATE = "Task updated successfully";
    public static final String TASK_DELETE = "Task deleted successfully";
    // ===Comment Messages ===
    public static final String COMMENT_CREATED = "Comment created successfully";
    public static final String COMMENT_FETCH = "Fetch Comment successfully";
    public static final String COMMENT_DELETE = "Comment deleted successfully";


    private AppConstants() {
        // prevent instantiation
    }

}
