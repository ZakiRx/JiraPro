package com.zgdev.jirapro.service;

import com.zgdev.jirapro.dto.TaskDTO;
import com.zgdev.jirapro.entity.Task;
import com.zgdev.jirapro.entity.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface TaskService {

     Optional<List<Task>> getTasks();
     Optional<Task> getTaskById(long id);
    Optional<List<Task>> getTasksByStatus(TaskStatus status);
     Optional<Task> createTask(Task task);
     Optional<Task> createTaskWithStatus(Task task, TaskStatus status);
     void removeTask(Task task);
     Optional<Task> updateTask(Task task);
     Optional<Task> updateTaskStatus(Task task, TaskStatus newStatus);
}
