package com.klm.taskmanagement.task.service;

import com.klm.taskmanagement.task.dto.TaskDto;
import com.klm.taskmanagement.task.dto.TaskRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing tasks.
 */
public interface TaskService {
    /**
     * Creates a new task.
     *
     * @param taskCreateDTO the DTO containing task creation details
     * @return the created TaskDTO
     */
    TaskDto createTask(TaskRequestDto taskCreateDTO);

    /**
     * Retrieves a task by its ID.
     *
     * @param id the task ID
     * @return the TaskDTO
     */
    TaskDto getTaskById(Long id);

    /**
     * Retrieves all tasks.
     *
     * @param pageable the pagination information (page number, size, sorting)
     * @return a Page of TaskDto objects
     */
    Page<TaskDto> getAllTasks(Pageable pageable);

    /**
     * Updates an existing task.
     *
     * @param id             the task ID
     * @param taskCreateDTO the updated task data
     * @return the updated TaskDTO
     */
    TaskDto updateTask(Long id, TaskRequestDto taskCreateDTO);

    /**
     * Deletes a task by its ID.
     *
     * @param id the task ID
     */
    void deleteTask(Long id);
}
