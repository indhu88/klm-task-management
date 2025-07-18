package com.klm.taskmanagement.task.entity;
/**
 * Enum representing the current status of a task in the system.
 * Used to track task progress.
 */
public enum TaskStatus {
    /** Task is created but work has not started yet. */
    TODO,
    /** Task is currently being worked on. */
    IN_PROGRESS,
    /** Task has been completed. */
    DONE
}
