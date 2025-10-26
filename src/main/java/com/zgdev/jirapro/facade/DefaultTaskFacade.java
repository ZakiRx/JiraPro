package com.zgdev.jirapro.facade;

import com.zgdev.jirapro.dto.TaskDTO;
import com.zgdev.jirapro.entity.Task;
import com.zgdev.jirapro.entity.TaskStatus;
import com.zgdev.jirapro.exception.TaskNotFoundException;
import com.zgdev.jirapro.mapper.TaskMapper;
import com.zgdev.jirapro.service.TaskService;
import com.zgdev.jirapro.service.TaskStatusService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class DefaultTaskFacade implements TaskFacade {
    private final TaskService taskService;
    private final TaskStatusService taskStatusService;
    private final TaskMapper taskMapper;

    public DefaultTaskFacade(TaskService taskService, TaskStatusService taskStatusService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskStatusService = taskStatusService;
        this.taskMapper = taskMapper;
    }

    @Override
    public List<TaskDTO> getTasks() {
        List<Task> tasks = taskService.getTasks().orElse(Collections.emptyList());
        return taskMapper.toDTOList(tasks);
    }
    @Override
    public List<TaskDTO> getTasksByStatus(String status) {
        TaskStatus taskStatus = taskStatusService
                .getTaskStatusByStatus(status)
                .orElseThrow(() -> new TaskNotFoundException("TaskStatus '" + status + "' not found"));
        List<Task> tasks = taskService.getTasksByStatus(taskStatus).orElse(Collections.emptyList());
        return taskMapper.toDTOList(tasks);
    }

    @Override
    public TaskDTO getTaskById(long id) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
        return taskMapper.toDTO(task);
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = taskMapper.toEntity(taskDTO);

        TaskStatus taskStatus = taskStatusService
                .getTaskStatusByStatus(taskDTO.completed().status())
                .orElseThrow(() -> new TaskNotFoundException("TaskStatus '" + taskDTO.completed().status() + "' not found"));

        Task savedTask = taskService.createTaskWithStatus(task, taskStatus)
                .orElseThrow(() -> new TaskNotFoundException("Failed to create task"));

        return taskMapper.toDTO(savedTask);
    }

    @Override
    public TaskDTO updateStatusOfTask(TaskDTO taskDTO) {
        Task task = taskService.getTaskById(taskDTO.id())
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + taskDTO.id() + " not found"));

        TaskStatus taskStatus = taskStatusService
                .getTaskStatusByStatus(taskDTO.completed().status())
                .orElseThrow(() -> new TaskNotFoundException("TaskStatus '" + taskDTO.completed().status() + "' not found"));

        Task updatedTask = taskService.updateTaskStatus(task, taskStatus)
                .orElseThrow(() -> new TaskNotFoundException("Failed to update task"));

        return taskMapper.toDTO(updatedTask);
    }

    @Override
    public void deleteTask(long id) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));

        taskService.removeTask(task);
    }
}