package com.klm.taskmanagement.task.repository;

import com.klm.taskmanagement.task.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Comment} entities.
 *
 * Extends {@link JpaRepository} to provide CRUD operations and
 * pagination/sorting functionality for Comment entities.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
     /**
      * Finds all comments by a specific task ID in a paginated format.
      *
      * @param taskId   the ID of the task
      * @param pageable the pagination information
      * @return a page of Comment entities
      */
Page<Comment> findByTaskId(Long taskId, Pageable pageable);
}
