package com.zgdev.jirapro.service.impl;

import com.zgdev.jirapro.entity.Task;
import com.zgdev.jirapro.entity.TaskStatus;
import com.zgdev.jirapro.repository.TaskRepository;
import com.zgdev.jirapro.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultTaskService implements TaskService {


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
        return Optional.of(taskRepository.save(task));
    }

    @Override
    public Optional<Task> createTaskWithStatus(Task task, TaskStatus status) {
        task.setCompleted(status);
        return Optional.of(taskRepository.save(task));
    }

    @Override
    public void removeTask(Task task) {
         taskRepository.delete(task);
    }

    @Override
    public Optional<Task> updateTask(Task task) {
        return Optional.of(taskRepository.save(task));
    }

    @Override
    public Optional<Task> updateTaskStatus(Task task, TaskStatus newStatus) {
        task.setCompleted(newStatus);
        return Optional.of(taskRepository.save(task));
    }
}
