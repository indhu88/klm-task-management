package com.klm.taskmanagement.task.controller;

import com.klm.taskmanagement.global.AppConstants;
import com.klm.taskmanagement.global.response.ApiResponse;
import com.klm.taskmanagement.task.dto.CommentDto;
import com.klm.taskmanagement.task.dto.CommentRequestDto;
import com.klm.taskmanagement.task.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling comment-related API requests.
 */
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    /**
     * Creates a new comment.
     *
     * @param commentRequestDto the request body containing comment data
     * @return response entity with created comment wrapped in ApiResponse
     */
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<CommentDto> createComment(
            @Valid @RequestBody CommentRequestDto commentRequestDto) {
        CommentDto created = commentService.createComment(commentRequestDto);
        return ApiResponse.created(AppConstants.COMMENT_CREATED, created);

    }

    /**
     * Retrieves comments by task ID.
     *
     * @param taskId the ID of the task whose comments to retrieve
     * @param page   the page number (0-based index), default is 0
     * @param size   the number of comments per page, default is 10
     * @return ApiResponse containing a Page of CommentDto
     */
    @GetMapping("/{taskId}/info")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Page<CommentDto>> getCommentsByTask(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<CommentDto> comments = commentService.getCommentsByTaskId(taskId, page, size);
        return ApiResponse.success(AppConstants.COMMENT_FETCH, comments);
    }
    /**
     * Deletes a comment by its ID.
     *
     * @param commentId the ID of the comment to delete
     * @return response entity with success message wrapped in ApiResponse
     */
    @DeleteMapping("/{commentId}/delete")
    @PreAuthorize("hasRole('USER') or @commentSecurity.isCommentAuthor(#commentId, authentication.name)")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(ApiResponse.success(AppConstants.COMMENT_DELETE, null));
    }

}
