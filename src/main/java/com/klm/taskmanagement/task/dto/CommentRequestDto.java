package com.klm.taskmanagement.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for handling comment creation requests.
 *
 * @param content  the content of the comment (cannot be blank)
 * @param taskId   the ID of the task to which the comment belongs (required)
 * @param authorId the ID of the user authoring the comment (required)
 */
public record CommentRequestDto(
        @NotBlank(message = "Comment must not be blank")
        @Size(max = 300, message = "Comment must be at most 300 characters")
        String content,

        @NotNull(message = "Task ID is required")
        Long taskId,

        @NotNull(message = "Author ID is required")
        Long authorId
) {}