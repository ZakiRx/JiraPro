package com.zgdev.jirapro.service.impl;

import com.zgdev.jirapro.entity.TaskStatus;
import com.zgdev.jirapro.repository.TaskStatusRepository;
import com.zgdev.jirapro.service.TaskStatusService;
import com.zgdev.jirapro.service.TaskStatusService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultTaskStatusService implements TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    public DefaultTaskStatusService(TaskStatusRepository taskStatusRepository) {
        this.taskStatusRepository = taskStatusRepository;
    }

    @Override
    public Optional<List<TaskStatus>> getTaskStatus() {
        return Optional.of(this.taskStatusRepository.findAll());
    }
    @Override
    public Optional<TaskStatus> getTaskStatusByStatus(String status) {
        return this.taskStatusRepository.getTaskStatusByStatus(status);
    }

    @Override
    public Optional<TaskStatus> getTaskStatusById(long id) {
        return this.taskStatusRepository.findById(id);
    }

    @Override
    public Optional<TaskStatus> createTaskStatus(TaskStatus taskStatus) {
        return Optional.of(this.taskStatusRepository.save(taskStatus));
    }

    @Override
    public Optional<TaskStatus> removeTaskStatus(TaskStatus taskStatus) {
        return Optional.empty();
    }

    @Override
    public Optional<TaskStatus> updateTaskStatus(int id) {
        return Optional.empty();
    }




}
