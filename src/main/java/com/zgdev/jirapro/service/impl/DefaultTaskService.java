package com.zgdev.jirapro.service.impl;

import com.zgdev.jirapro.entity.Task;
import com.zgdev.jirapro.entity.TaskStatus;
import com.zgdev.jirapro.exception.OperationException;
import com.zgdev.jirapro.repository.TaskRepository;
import com.zgdev.jirapro.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultTaskService implements TaskService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultTaskService.class);

    private final TaskRepository taskRepository;

    public DefaultTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Optional<List<Task>> getTasks() {
        return Optional.of(taskRepository.findAll());
    }

    @Override
    public Optional<Task> getTaskById(long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Optional<List<Task>> getTasksByStatus(TaskStatus status) {
        return taskRepository.getTasksByCompleted(status);
    }

    @Override
    public Optional<Task> createTask(Task task) {
        try {
            return Optional.of(taskRepository.save(task));
        } catch (Exception e) {
            LOG.error("Unexpected error creating task: {}", task, e);
            throw new OperationException("Failed to create task");
        }
    }

    @Override
    public Optional<Task> createTaskWithStatus(Task task, TaskStatus status) {
        try {
            task.setCompleted(status);
            return Optional.of(taskRepository.save(task));
        } catch (Exception e) {
            LOG.error("Unexpected error creating task with status: {}", task, e);
            throw new OperationException("Failed to create task");
        }
    }

    @Override
    public void removeTask(Task task) {
        try {
            taskRepository.delete(task);
        } catch (Exception e) {
            LOG.error("Unexpected error deleting task: {}", task.getId(), e);
            throw new OperationException("Failed to delete task");
        }
    }

    @Override
    public Optional<Task> updateTask(Task task) {
        try {
            return Optional.of(taskRepository.save(task));
        } catch (Exception e) {
            LOG.error("Unexpected error updating task: {}", task, e);
            throw new OperationException("Failed to update task");
        }
    }

    @Override
    public Optional<Task> updateTaskStatus(Task task, TaskStatus newStatus) {
        try {
            task.setCompleted(newStatus);
            return Optional.of(taskRepository.save(task));
        } catch (Exception e) {
            LOG.error("Unexpected error updating task status: {}", task.getId(), e);
            throw new OperationException("Failed to update task status");
        }
    }
}
