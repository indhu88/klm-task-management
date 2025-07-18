package com.klm.taskmanagement.task.service;

import com.klm.taskmanagement.global.exception.ResourceNotFoundException;
import com.klm.taskmanagement.task.dto.CommentDto;
import com.klm.taskmanagement.task.dto.CommentRequestDto;
import com.klm.taskmanagement.task.entity.Comment;
import com.klm.taskmanagement.task.entity.Task;
import com.klm.taskmanagement.task.repository.CommentRepository;
import com.klm.taskmanagement.task.repository.TaskRepository;
import com.klm.taskmanagement.user.entity.User;
import com.klm.taskmanagement.user.repository.UserRepository;
import com.klm.taskmanagement.websocket.NotificationMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CommentServiceImpl}, verifying CRUD logic with mocked repositories.
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private CommentServiceImpl commentService;
    @Mock
    private UserRepository userRepository; // ✅ Add this

    /**
     * Verifies that a new comment is saved and returned correctly.
     */
    @Test
    void createComment_success() {
        Long taskId = 1L;
        Long userId = 1L; //
        Task task = new Task();
        task.setId(taskId);
        User author = new User();
        author.setId(userId);
        author.setUsername("testuser");
        CommentRequestDto request = new CommentRequestDto( "Test comment", 1L,taskId);

        Comment savedComment = new Comment();
        savedComment.setId(1L);
        savedComment.setContent("Test comment");
        savedComment.setTask(task);
        savedComment.setAuthor(author);
        savedComment.setCreatedAt(LocalDateTime.now());

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findById(userId
        )).thenReturn(Optional.of(author)); // ✅ fix
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        CommentDto result = commentService.createComment(request);

        assertThat(result).isNotNull();
        assertThat(result.comment()).isEqualTo("Test comment");
        verify(commentRepository).save(any(Comment.class));

    }

    /**
     * Verifies retrieval of an existing comment.
     */
    @Test
    void getCommentById_found() {
        Task task = new Task();
        task.setId(1L);
// Mock Author
        User author = new User();
        author.setId(99L);
        author.setUsername("testuser");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Hello");
        comment.setTask(task);
        comment.setAuthor(author);  // ✅ This line fixes the NPE
        comment.setCreatedAt(LocalDateTime.now());

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        CommentDto result = commentService.getCommentById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.comment()).isEqualTo("Hello");
        assertThat(result.taskId()).isEqualTo(1L);
    }
    /**
     * Verifies that requesting a missing comment throws ResourceNotFoundException.
     */
    @Test
    void getCommentById_notFound_throws() {
        when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.getCommentById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Comment not found");
    }

    /**
     * Verifies that an existing comment is deleted.
     */
    @Test
    void deleteComment_callsRepository() {
        Comment comment = new Comment();
        comment.setId(5L);

        when(commentRepository.findById(5L)).thenReturn(Optional.of(comment));

        commentService.deleteComment(5L);

        verify(commentRepository).delete(comment);

    }

    /**
     * Verifies that deleting a missing comment throws ResourceNotFoundException.
     */
    @Test
    void deleteComment_notFound_throws() {
        when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.deleteComment(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Comment not found");

        verify(commentRepository, never()).delete(any());
    }
}