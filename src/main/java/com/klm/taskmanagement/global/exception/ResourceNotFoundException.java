/**
 * This package contains custom exception classes and handlers
 * for managing error scenarios within the user module
 * of the task management system.
 */
package com.klm.taskmanagement.global.exception;
/**
 * Exception thrown when a requested resource cannot be found.
 *
 * This is a runtime exception that signals the absence of a resource,
 * such as a user, entity, or record that was expected to exist.
 *
 * Typically handled globally to return a 404 Not Found HTTP response.
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Constructs a new ResourceNotFoundException with no detail message.
     */
    public ResourceNotFoundException() {
        super();
    }
    /**
     * Constructs a new ResourceNotFoundException with the specified detail message
     * and cause.
     *
     * @param message The detail message explaining the reason for the exception.
     * @param cause   The cause (which is saved for later retrieval).
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
    /**
     * Constructs a new ResourceNotFoundException with the specified cause.
     *
     * @param cause The cause (which is saved for later retrieval).
     */
    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
