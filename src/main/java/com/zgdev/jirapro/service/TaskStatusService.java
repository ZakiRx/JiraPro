package com.zgdev.jirapro.service;

import com.zgdev.jirapro.entity.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface TaskStatusService {
    Optional<List<TaskStatus>> getTaskStatus();
    Optional<TaskStatus> getTaskStatusById(long id);
    Optional<TaskStatus> createTaskStatus(TaskStatus TaskStatus);
    void removeTaskStatus(TaskStatus TaskStatus);
    Optional<TaskStatus>  getTaskStatusByStatus(String status);
}
