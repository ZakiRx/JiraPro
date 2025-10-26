package com.zgdev.jirapro.facade;

import com.zgdev.jirapro.dto.TaskStatusDTO;

import java.util.List;
import java.util.Optional;


public interface TaskStatusFacade {

    TaskStatusDTO createTaskStatus(TaskStatusDTO TaskStatusDTO);
    List<TaskStatusDTO> getTaskStatus();
}
