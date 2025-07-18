package com.klm.taskmanagement.task.dto;

import com.klm.taskmanagement.task.entity.TaskPriority;
import com.klm.taskmanagement.task.entity.TaskStatus;

import java.time.LocalDate;


/**
 * Record for sending or validating task data.
 *
 * @param id
 * @param title
 * @param description
 * @param status
 * @param priority
 * @param targetDate
 */
public record TaskDto(

        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,

        LocalDate targetDate

) {
}