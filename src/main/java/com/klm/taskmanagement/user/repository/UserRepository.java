/**
 * This package contains repository interfaces responsible for
 * data access and persistence operations related to user entities
 * in the task management system.
 */
package com.klm.taskmanagement.user.repository;

import com.klm.taskmanagement.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Repository interface for User entities.
 *
 * Extends JpaRepository to provide CRUD operations and
 * custom query methods for User data.
 *
 * The @Repository annotation indicates that this interface
 * is a Spring-managed bean responsible for data access.
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    /**
     * Finds a user by their unique username.
     *
     * @param username The username to search for.
     * @return An Optional containing the User if found, or empty if not.
     */
    Optional<User> findByUsername(String username);
    /**
     * Finds a user by their unique username.
     *
     * @param username The username to search for.
     * @return An Optional containing the User if found, or empty if not.
     */
    boolean existsByUsername(String username);
    /**
     * Checks if a user with the given email exists.
     *
     * @param email The email to check.
     * @return True if a user with the email exists, false otherwise.
     */
    boolean existsByEmail(String email);
}
