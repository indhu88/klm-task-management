package com.klm.taskmanagement.task.entity;

import com.klm.taskmanagement.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * Entity representing a comment on a task.
 * Each comment is linked to one task and one user (author).
 */
@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    /**
     * Unique identifier for the comment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The content of the comment.
     * Cannot be blank.
     */
    @NotBlank
    @Column(nullable = false)
    private String content;
    /**
     * Timestamp when the comment was created.
     * Automatically set before persisting.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    /**
     * The task this comment belongs to.
     * Many comments can be associated with one task.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "task_id")
    private Task task;
    /**
     * The user who authored the comment.
     * Many comments can be authored by one user.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User author;
    /**
     * JPA lifecycle callback to set createdAt timestamp before saving.
     */
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
    @Version
    private int version;
}
