package com.klm.taskmanagement.task.service;

import com.klm.taskmanagement.task.dto.CommentDto;
import com.klm.taskmanagement.task.dto.CommentRequestDto;
import org.springframework.data.domain.Page;

/**
 * Service interface for managing comments on tasks.
 */
public interface CommentService {
    /**
     * Creates a new comment based on the provided request data.
     *
     * @param commentRequestDto the data to create a comment
     * @return the created comment as a DTO
     */
    CommentDto createComment(CommentRequestDto commentRequestDto);

    /**
     * Retrieves all comments for a given task in a paginated format.
     *
     * @param taskId the ID of the task
     * @param page   the page number (0-based)
     * @param size   the size of the page
     * @return a Page of CommentDto objects
     */
    Page<CommentDto> getCommentsByTaskId(Long taskId, int page, int size);

    /**
     * Deletes a comment by its ID.
     *
     * @param commentId the ID of the comment to delete
     */
    void deleteComment(Long commentId);

    /**
     * Retrieves a comment by its ID.
     *
     * @param commentId the ID of the comment
     * @return the comment DTO
     */
    CommentDto getCommentById(Long commentId);
}
