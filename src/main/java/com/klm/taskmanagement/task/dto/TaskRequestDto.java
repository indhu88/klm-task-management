package com.klm.taskmanagement.task.dto;

import com.klm.taskmanagement.task.entity.TaskPriority;
import com.klm.taskmanagement.task.entity.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * DTO for creating or updating a Task.
 * <p>
 * This record is used to transfer task data in create/update operations.
 *
 * @param id          Unique identifier of the task (optional during creation).
 * @param title       Title of the task. Cannot be blank.
 * @param description Detailed description of the task.
 * @param status      The current status of the task.
 * @param priority    The priority level of the task.
 * @param targetDate  The date by which the task is due. Must be today or a future date.
 */
public record TaskRequestDto(

        Long id,

        @NotBlank(message = "Title is mandatory")
        @Size(max = 100, message = "Title must be at most 100 characters")
        String title,
        @Size(max = 500, message = "Description must be at most 500 characters")
        String description,

        @NotNull(message = "Status must be provided")
        TaskStatus status,

        @NotNull(message = "Priority must be provided")
        TaskPriority priority,

        @NotNull(message = "Target date is required")
        @FutureOrPresent(message = "Target date must be today or in the future")
        LocalDate targetDate,@NotNull(message = "Assigned user ID is required")
        Long assignedUserId)

{
}
