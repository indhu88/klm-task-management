package com.klm.taskmanagement.task.repository;

import com.klm.taskmanagement.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Task} entities.
 *
 * Extends {@link JpaRepository} to provide CRUD operations and
 * pagination/sorting functionality for Task entities.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {
    // Add custom query methods here if needed, for example:
    // List<Task> findByStatus(String status);
}
