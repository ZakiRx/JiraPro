package com.zgdev.jirapro.facade;

import com.zgdev.jirapro.dto.TaskDTO;

import java.util.List;
import java.util.Optional;

public interface TaskFacade {

    List<TaskDTO>  getTasks();
    List<TaskDTO> getTasksByStatus(String status);
    TaskDTO getTaskById(long id);
    TaskDTO createTask(TaskDTO taskDTO);
    TaskDTO updateStatusOfTask(TaskDTO taskDTO);
    void deleteTask(long id);

}
