package com.klm.taskmanagement.config;

import com.klm.taskmanagement.task.entity.Comment;
import com.klm.taskmanagement.task.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Security component used to evaluate access permissions for comment-related actions.
 * <p>
 * This is used in Spring Security expressions (e.g., { @PreAuthorize})
 * to ensure that users can only modify or delete their own comments unless they are admins.
 */
@Component("commentSecurity")
@RequiredArgsConstructor
public class CommentSecurityConfig {
    private final CommentRepository commentRepository;
    /**
     * Checks whether the user identified by the given username is the author of the specified comment.
     *
     * @param commentId the ID of the comment to check
     * @param username  the username (usually email or login ID) of the authenticated user
     * @return {@code true} if the user is the author of the comment; {@code false} otherwise
     */
    public boolean isCommentAuthor(Long commentId, String username) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        return optionalComment
                .map(comment -> comment.getAuthor().getUsername().equals(username))
                .orElse(false);
    }
}
