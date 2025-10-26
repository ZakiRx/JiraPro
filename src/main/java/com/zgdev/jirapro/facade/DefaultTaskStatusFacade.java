package com.zgdev.jirapro.facade;

import com.zgdev.jirapro.dto.TaskStatusDTO;
import com.zgdev.jirapro.entity.TaskStatus;
import com.zgdev.jirapro.exception.OperationException;
import com.zgdev.jirapro.mapper.TaskStatusMapper;
import com.zgdev.jirapro.service.TaskStatusService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultTaskStatusFacade implements TaskStatusFacade {

    private final TaskStatusService taskStatusService;
    private final TaskStatusMapper taskStatusMapper;

    public DefaultTaskStatusFacade(TaskStatusService taskStatusService, TaskStatusMapper taskStatusMapper) {
        this.taskStatusService = taskStatusService;
        this.taskStatusMapper = taskStatusMapper;
    }

    @Override
    public TaskStatusDTO createTaskStatus(TaskStatusDTO taskStatusDTO) {
        TaskStatus taskStatus = taskStatusMapper.toEntity(taskStatusDTO);
        TaskStatus savedTaskStatus = taskStatusService.createTaskStatus(taskStatus).orElseThrow(()-> new OperationException("TaskStatus not created"));
        return taskStatusMapper.toDTO(savedTaskStatus);
    }

    @Override
    public List<TaskStatusDTO> getTaskStatus() {
        return taskStatusMapper.toDTOList(taskStatusService.getTaskStatus());
    }
}
