package com.zgdev.jirapro.service.impl;

import com.zgdev.jirapro.entity.TaskStatus;
import com.zgdev.jirapro.exception.OperationException;
import com.zgdev.jirapro.repository.TaskStatusRepository;
import com.zgdev.jirapro.service.TaskStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultTaskStatusService implements TaskStatusService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultTaskStatusService.class);

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
        try {
            return Optional.of(this.taskStatusRepository.save(taskStatus));
        } catch (Exception e) {
            LOG.error("Unexpected error creating task status: {}", taskStatus, e);
            throw new OperationException("Failed to create task status");
        }
    }

    @Override
    public Optional<TaskStatus> removeTaskStatus(TaskStatus taskStatus) {
        try {
            this.taskStatusRepository.delete(taskStatus);
            return Optional.of(taskStatus);
        } catch (Exception e) {
            LOG.error("Unexpected error deleting task status: {}", taskStatus.getId(), e);
            throw new OperationException("Failed to delete task status");
        }
    }

}
