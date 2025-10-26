package com.zgdev.jirapro.service;

import com.zgdev.jirapro.entity.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface TaskStatusService {
    Optional<List<TaskStatus>> getTaskStatus();
    Optional<TaskStatus> getTaskStatusById(long id);
    Optional<TaskStatus> createTaskStatus(TaskStatus TaskStatus);
    Optional<TaskStatus> removeTaskStatus(TaskStatus TaskStatus);
    Optional<TaskStatus> updateTaskStatus(int id);
    Optional<TaskStatus>  getTaskStatusByStatus(String status);
}
