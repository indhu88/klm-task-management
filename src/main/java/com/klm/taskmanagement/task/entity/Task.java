package com.klm.taskmanagement.task.entity;

import com.klm.taskmanagement.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


/**
 * Entity representing a task in the Task Management System.
 * A task can be assigned to a user, has a due date, and may contain comments.
 */
@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    /**
     * Unique identifier for the task.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Title of the task. Cannot be blank.
     */
    @NotBlank
    @Column(nullable = false)
    private String title;
    /**
     * Optional detailed description of the task.
     */
    private String description;
    /**
     * Current status of the task.
     * Default is TODO.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.TODO;  // Default status
    /**
     * Priority level of the task.
     * Default is MEDIUM.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority = TaskPriority.MEDIUM;  // Default priority

    /**
     * Due date for the task.
     * Must be in the present or future.
     * Prevent setting target date in past
     */
    @FutureOrPresent
    @Column(nullable = false)
    private LocalDate targetDate;
    /**
     * The user to whom the task is assigned.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;
    /**
     * List of comments associated with this task.
     * Cascade operations and orphan removal are enabled.
     */
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
    @Version
    private int version;
}
