package com.zgdev.jirapro.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zgdev.jirapro.dto.TaskDTO;
import com.zgdev.jirapro.entity.Task;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskMapper {

    private final ObjectMapper objectMapper;

    public TaskMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public TaskDTO toDTO(Task task) {
        if (task == null) {
            return null;
        }
        return objectMapper.convertValue(task, TaskDTO.class);
    }

    public Task toEntity(TaskDTO taskDTO) {
        if (taskDTO == null) {
            return null;
        }
        return objectMapper.convertValue(taskDTO, Task.class);
    }

    public List<TaskDTO> toDTOList(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return List.of();
        }
        return objectMapper.convertValue(
            tasks,
            objectMapper.getTypeFactory().constructCollectionType(List.class, TaskDTO.class)
        );
    }
}