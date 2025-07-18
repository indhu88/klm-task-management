package com.klm.taskmanagement.task.service;

import com.klm.taskmanagement.global.exception.ResourceNotFoundException;
import com.klm.taskmanagement.task.dto.TaskDto;
import com.klm.taskmanagement.task.dto.TaskRequestDto;
import com.klm.taskmanagement.task.entity.Task;
import com.klm.taskmanagement.task.entity.TaskPriority;
import com.klm.taskmanagement.task.entity.TaskStatus;
import com.klm.taskmanagement.task.repository.TaskRepository;
import com.klm.taskmanagement.user.entity.Role;
import com.klm.taskmanagement.user.entity.User;
import com.klm.taskmanagement.user.repository.UserRepository;
import com.klm.taskmanagement.websocket.NotificationMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link TaskServiceImpl} using Mockito for mocking dependencies.
 * <p>
 * This test class verifies the business logic in TaskServiceImpl by mocking the TaskRepository.
 * It covers CRUD operations and exception scenarios.
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private TaskServiceImpl taskService;
    @Mock
    private UserRepository userRepository; // ✅ Add this

    /**
     * Tests that creating a task saves the entity and returns a corresponding TaskDto.
     */
    @Test
    void createTask_shouldSaveAndReturnTaskDto() {
        Long assignedUserId = 1L; //
        Long taskId=1L;
        TaskRequestDto requestDto = new TaskRequestDto(
                taskId,
                "Title",
                "Description",
                TaskStatus.DONE,
                TaskPriority.HIGH,
                LocalDate.of(2025, 12, 31),assignedUserId
        );

        User assignedUser = new User();
        assignedUser.setId(assignedUserId);
        assignedUser.setUsername("testuser");
        Task savedTask = new Task();
        savedTask.setId(taskId);
        savedTask.setTitle(requestDto.title());
        savedTask.setDescription(requestDto.description());
        savedTask.setStatus(requestDto.status());
        savedTask.setPriority(requestDto.priority());
        savedTask.setTargetDate(requestDto.targetDate());
        savedTask.setAssignedUser(assignedUser);
        when(userRepository.findById(assignedUserId)).thenReturn(Optional.of(assignedUser));
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskDto result = taskService.createTask(requestDto);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.title()).isEqualTo("Title");
        verify(taskRepository).save(any(Task.class));
    }

    /**
     * Tests that fetching a task by ID returns a TaskDto if the task exists.
     */
    @Test
    void getTaskById_whenFound_shouldReturnTaskDto() {
        Long assignedUserId = 1L; //
        User assignedUser = new User();
        assignedUser.setId(assignedUserId);
        assignedUser.setUsername("testuser");
        Long taskId=1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Test Task");
task.setAssignedUser(assignedUser);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        TaskDto result = taskService.getTaskById(taskId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(taskId);
        assertThat(result.title()).isEqualTo("Test Task");
    }

    /**
     * Tests that fetching a task by a non-existent ID throws ResourceNotFoundException.
     */
    @Test
    void getTaskById_whenNotFound_shouldThrowException() {
        Long taskId=1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(taskId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found with ID: 1");
    }
    @Test
    void deleteTask_ShouldThrowAccessDenied_WhenAssignedUserIsNull() {
        // Arrange
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Test Task");
        task.setAssignedUser(null); // Simulate missing user

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Act & Assert
        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> taskService.deleteTask(taskId)
        );

        assertEquals("Cannot delete task without an assigned user.", exception.getMessage());

        verify(taskRepository, never()).delete(any(Task.class));
    }
    /**
     * Tests that getAllTasks returns a paginated list of TaskDto objects.
     */
    @Test
    void getAllTasks_withPagination_shouldReturnPagedResult() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");

        Pageable pageable = PageRequest.of(0, 2);
        Page<Task> taskPage = new PageImpl<>(List.of(task1, task2), pageable, 2);

        when(taskRepository.findAll(pageable)).thenReturn(taskPage);

        Page<TaskDto> result = taskService.getAllTasks(pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).id()).isEqualTo(1L);
        assertThat(result.getContent().get(1).title()).isEqualTo("Task 2");
    }

    /**
     * Tests that updating an existing task applies changes and returns updated TaskDto.
     */
    @Test
    void updateTask_whenFound_shouldUpdateAndReturnDto() {
        Task existingTask = new Task();
        Long taskId=1L;

        Long mockUserId = 1L;
        User mockUser = new User();
        mockUser.setId(mockUserId);
        mockUser.setUsername("testuser");
        mockUser.setEmail("test@example.com");
        existingTask.setId(taskId);
        existingTask.setTitle("Old Title");
        existingTask.setAssignedUser(mockUser);
        TaskRequestDto requestDto = new TaskRequestDto(
                taskId,
                "Title",
                "Description",
                TaskStatus.DONE,
                TaskPriority.HIGH,
                LocalDate.of(2025, 12, 31),mockUserId
        );

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(mockUser));

        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        TaskDto result = taskService.updateTask(taskId, requestDto);

        assertThat(result.title()).isEqualTo("Title");
        assertThat(result.status()).isEqualTo(TaskStatus.DONE);
        verify(taskRepository).save(existingTask);
    }

    /**
     * Tests that updating a non-existent task throws ResourceNotFoundException.
     */
    @Test
    void updateTask_whenNotFound_shouldThrowException() {
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        TaskRequestDto updateDto = new TaskRequestDto(
                taskId, "Title", "Desc", TaskStatus.TODO, TaskPriority.LOW, LocalDate.now(),
        1L);

        assertThatThrownBy(() -> taskService.updateTask(taskId, updateDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found with ID: 1");
    }

    /**
     * Tests that deleting an existing task calls repository delete method and broadcasts via WebSocket.
     */
    @Test
    void deleteTask_whenFound_shouldDelete() {
        Long taskId=1L;

        Long mockUserId = 1L;
        User mockUser = new User();
        mockUser.setId(mockUserId);
        mockUser.setUsername("testuser");
        mockUser.setEmail("test@example.com");
        mockUser.setRoles(Set.of(Role.ROLE_ADMIN));

        Task task = new Task();
        task.setId(taskId);
        task.setAssignedUser(mockUser); // ✅ This is enough

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));



        taskService.deleteTask(taskId);

        verify(taskRepository).delete(task);
        verify(messagingTemplate).convertAndSend(eq("/topic/updates"), any(NotificationMessage.class));
    }

    /**
     * Tests that deleting a non-existent task throws ResourceNotFoundException.
     */
    @Test
    void deleteTask_whenNotFound_shouldThrowException() {
        Long taskId=1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.deleteTask(taskId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found with ID: 1"); // ✅ no space
    }
    @Test
    void updateTask_shouldUpdateTaskAndAssignedUser() {
        Long taskId = 1L;
        Long userId = 2L;

        TaskRequestDto dto = new TaskRequestDto(
                taskId,                            // assignedUserId
                "Updated Title",
                "Updated Description",
                TaskStatus.IN_PROGRESS,
                TaskPriority.HIGH,
                LocalDate.of(2025, 12, 31),userId
        );

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        existingTask.setStatus(TaskStatus.TODO);
        existingTask.setPriority(TaskPriority.MEDIUM);
        existingTask.setTargetDate(LocalDate.now());
        existingTask.setAssignedUser(new User());  // Initial assigned user

        User newAssignedUser = User.builder()
                .id(userId)
                .username("testuser")
                .build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(userRepository.findById(userId)).thenReturn(Optional.of(newAssignedUser));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskDto result = taskService.updateTask(taskId, dto);

        assertNotNull(result);
        assertEquals(dto.title(), result.title());
        assertEquals(dto.status(), result.status());
        assertEquals(dto.priority(), result.priority());
        assertEquals(dto.targetDate(), result.targetDate());

        verify(taskRepository).findById(taskId);
        verify(userRepository).findById(userId);
        verify(taskRepository).save(any(Task.class));
    }
}
