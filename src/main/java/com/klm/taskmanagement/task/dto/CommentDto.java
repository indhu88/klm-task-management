package com.klm.taskmanagement.task.dto;


import java.time.LocalDateTime;

/**
 * DTO for transferring comment data to clients.
 * Used in responses to represent comment details.
 *
 * @param id        the unique ID of the comment
 * @param comment   the textual content of the comment
 * @param createdAt the timestamp when the comment was created
 * @param taskId    the ID of the task this comment belongs to
 * @param authorId  the ID of the user who created the comment
 */
public record CommentDto(
        Long id,
        String comment,
        LocalDateTime createdAt,
        Long taskId,
        Long authorId
) {}
