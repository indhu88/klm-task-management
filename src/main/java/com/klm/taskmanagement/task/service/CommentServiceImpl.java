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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Implementation of {@link CommentService} to manage comment-related business logic.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    /**
     * {@inheritDoc}
     */
    @Override
    public CommentDto createComment(CommentRequestDto dto) {
        Task task = taskRepository.findById(dto.taskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        User author = userRepository.findById(dto.authorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        Comment comment = new Comment();
        comment.setContent(dto.content());
        comment.setTask(task);
        comment.setAuthor(author);
        comment.setCreatedAt(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);

        return mapToDto(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<CommentDto> getCommentsByTaskId(Long taskId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return commentRepository.findByTaskId(taskId, pageable)
                .map(this::mapToDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteComment(Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        commentRepository.delete(comment);


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommentDto getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .map(this::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
    }

    /**
     * Maps a {@link Comment} entity to its DTO representation.
     *
     * @param comment the comment entity
     * @return the comment DTO
     */
    private CommentDto mapToDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getTask().getId(),
                comment.getAuthor().getId()
        );
    }
}
