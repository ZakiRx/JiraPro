package com.zgdev.jirapro.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zgdev.jirapro.dto.TaskStatusDTO;
import com.zgdev.jirapro.entity.TaskStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TaskStatusMapper {

    private final ObjectMapper objectMapper;

    public TaskStatusMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public TaskStatusDTO toDTO(TaskStatus taskStatus) {
        if (taskStatus == null) {
            return null;
        }
        return objectMapper.convertValue(taskStatus, TaskStatusDTO.class);
    }

    public TaskStatusDTO toDTO(Optional<TaskStatus> taskStatus) {
        return taskStatus.map(this::toDTO).orElse(null);
    }

    public TaskStatus toEntity(TaskStatusDTO taskStatusDTO) {
        if (taskStatusDTO == null) {
            return null;
        }
        return objectMapper.convertValue(taskStatusDTO, TaskStatus.class);
    }

    public List<TaskStatusDTO> toDTOList(List<TaskStatus> taskStatuses) {
        if (taskStatuses == null || taskStatuses.isEmpty()) {
            return List.of();
        }
        return objectMapper.convertValue(taskStatuses, new TypeReference<List<TaskStatusDTO>>() {});
    }

    public List<TaskStatusDTO> toDTOList(Optional<List<TaskStatus>> taskStatuses) {
        return taskStatuses.map(this::toDTOList).orElse(List.of());
    }
}