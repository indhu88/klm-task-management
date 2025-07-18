package com.klm.taskmanagement.task.service;

import com.klm.taskmanagement.global.AppConstants;
import com.klm.taskmanagement.global.exception.ConflictException;
import com.klm.taskmanagement.global.exception.ResourceNotFoundException;
import com.klm.taskmanagement.task.dto.TaskDto;
import com.klm.taskmanagement.task.dto.TaskRequestDto;
import com.klm.taskmanagement.task.entity.Task;
import com.klm.taskmanagement.task.repository.TaskRepository;
import com.klm.taskmanagement.user.entity.Role;
import com.klm.taskmanagement.user.entity.User;
import com.klm.taskmanagement.user.repository.UserRepository;
import com.klm.taskmanagement.websocket.NotificationMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Implementation of the {@link TaskService} interface that provides
 * business logic for managing tasks.
 * Also integrates WebSocket support to send real-time notifications when task changes occur.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;


    /**
     * {@inheritDoc}
     */
    @Override
    public TaskDto createTask(TaskRequestDto taskCreateDTO) {
        Task task = new Task();
        mapToEntity(taskCreateDTO, task);
        // 🔔 Notify WebSocket subscribers of the new task
        messagingTemplate.convertAndSend(
                "/topic/updates",
                new NotificationMessage("🆕 Task created: " + taskCreateDTO.title())
        );
        return toDTO(taskRepository.save(task));
    }

    /**
     * Converts a Task entity to TaskDto record.
     *
     * @param task the Task entity
     * @return the TaskDto record
     */
    private TaskDto toDTO(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getTargetDate()
        );

    }

    /**
     * Finds a task by its ID or throws a {@link ResourceNotFoundException}.
     *
     * @param id the task ID
     * @return the found task
     */
    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.TASK_NOT_FOUND + id));
    }

    @Override
    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.TASK_NOT_FOUND + id));
        return toDTO(task);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Page<TaskDto> getAllTasks(Pageable pageable) {

        return taskRepository.findAll(pageable)
                .map(this::toDTO);

    }

    @Override
    public TaskDto updateTask(Long id, TaskRequestDto dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.TASK_NOT_FOUND + id));

        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setTargetDate(dto.targetDate());
        task.setStatus(dto.status());
        task.setPriority(dto.priority());
        // ✅ Update assigned user if needed
        User user = userRepository.findById(dto.assignedUserId())
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + dto.assignedUserId()));
        task.setAssignedUser(user);
        try {
            taskRepository.save(task);
            // 🔔 Notify clients of task update
            messagingTemplate.convertAndSend(
                    "/topic/updates",
                    new NotificationMessage("✏️ Task updated: " + dto.title())
            );
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new ConflictException(AppConstants.CONFLICT_EXCEPTION);
        }

        return toDTO(task);
    }

    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.TASK_NOT_FOUND + id));
        User taskAssignedUser = task.getAssignedUser();

        if (taskAssignedUser == null) {
            throw new AccessDeniedException("Cannot delete task without an assigned user.");
        }

        Set<Role> roles = taskAssignedUser.getRoles();
        if (roles == null || (roles.size() == 1 && roles.contains(Role.ROLE_USER))) {
            throw new AccessDeniedException(AppConstants.ACCESS_DENIED);
        }
        taskRepository.delete(task);
        // 🔔 Notify clients of task deletion
        messagingTemplate.convertAndSend(
                "/topic/updates",
                new NotificationMessage("❌ Task deleted: " + task.getTitle())
        );
    }

    /**
     * Maps a {@link TaskRequestDto} to a {@link Task} entity.
     *
     * @param dto  the DTO to map from
     * @param task the Task entity to update
     */
    private void mapToEntity(TaskRequestDto dto, Task task) {
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setStatus(dto.status());
        task.setPriority(dto.priority());
        task.setTargetDate(dto.targetDate());
        // Add this if TaskRequestDto has assignedUserId
        User user = userRepository.findById(dto.assignedUserId())
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_AVAILABLE + dto.assignedUserId()));
        task.setAssignedUser(user);
    }
}
