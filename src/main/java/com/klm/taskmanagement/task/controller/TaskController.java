package com.klm.taskmanagement.task.controller;

import com.klm.taskmanagement.global.AppConstants;
import com.klm.taskmanagement.global.response.ApiResponse;
import com.klm.taskmanagement.task.dto.TaskDto;
import com.klm.taskmanagement.task.dto.TaskRequestDto;
import com.klm.taskmanagement.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing tasks.
 * <p>
 * Provides endpoints for creating, retrieving, updating, and deleting tasks.
 * Secured with role-based access control using Spring Security.
 */
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * Creates a new task.
     *
     * @param request the task creation request DTO
     * @return ApiResponse containing the created task DTO and success message
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ApiResponse<TaskDto> createTask(@Valid @RequestBody TaskRequestDto request) {
        TaskDto response = taskService.createTask(request);
        return ApiResponse.created(AppConstants.TASK_CREATED, response);
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id the task ID
     * @return ApiResponse containing the task DTO if found
     */
    @GetMapping("/{id}/info")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public  ApiResponse<TaskDto> getTaskById(@PathVariable Long id) {
        TaskDto task = taskService.getTaskById(id);
        return ApiResponse.success(AppConstants.TASK_FETCH, task);
    }

    /**
     * Retrieves all tasks in the system.
     * <p>
     * Accessible only to users with the ADMIN role.
     * @param page the page number (0-based index), default is 0
     * @param size the number of tasks per page, default is 10
     * @return ApiResponse containing a Page of TaskDto
     */
    @GetMapping("/all-tasks")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Page<TaskDto>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<TaskDto> tasks = taskService.getAllTasks(PageRequest.of(page, size));
        return ApiResponse.success(AppConstants.TASK_FETCH, tasks);
    }

    /**
     * Updates an existing task by ID.
     *
     * @param id      the task ID
     * @param request the task update request DTO
     * @return ApiResponse containing the updated task DTO
     */
    @PutMapping("/{id}/update")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ApiResponse<TaskDto> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDto request) {
        TaskDto updateTask = taskService.updateTask(id, request);
        return ApiResponse.success(AppConstants.TASK_UPDATE, updateTask);

    }

    /**
     * Deletes a task by its ID.
     * <p>
     * Accessible only to users with the ADMIN role.
     *
     * @param id the task ID
     * @return ApiResponse indicating success or failure of the deletion
     */
    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(ApiResponse.success(AppConstants.TASK_DELETE, null));
    }
}