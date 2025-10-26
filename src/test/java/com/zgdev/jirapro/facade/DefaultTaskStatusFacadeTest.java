package com.zgdev.jirapro.facade;

import com.zgdev.jirapro.dto.TaskStatusDTO;
import com.zgdev.jirapro.entity.TaskStatus;
import com.zgdev.jirapro.mapper.TaskStatusMapper;
import com.zgdev.jirapro.service.TaskStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultTaskStatusFacadeTest {

    @Mock
    private TaskStatusService taskStatusService;

    @Mock
    private TaskStatusMapper taskStatusMapper;

    @InjectMocks
    private DefaultTaskStatusFacade taskStatusFacade;

    private TaskStatus taskStatus1;
    private TaskStatus taskStatus2;
    private TaskStatusDTO taskStatusDTO1;
    private TaskStatusDTO taskStatusDTO2;

    @BeforeEach
    void setUp() {
        taskStatus1 = new TaskStatus();
        taskStatus1.setId(1L);
        taskStatus1.setStatus("TO_DO");

        taskStatus2 = new TaskStatus();
        taskStatus2.setId(2L);
        taskStatus2.setStatus("DONE");

        taskStatusDTO1 = new TaskStatusDTO(1L, "TO_DO");
        taskStatusDTO2 = new TaskStatusDTO(2L, "DONE");
    }

    @Test
    void shouldCreateTaskStatus() {
        TaskStatusDTO inputDTO = new TaskStatusDTO(0L, "IN_PROGRESS");

        TaskStatus inputStatus = new TaskStatus();
        inputStatus.setStatus("IN_PROGRESS");

        TaskStatus savedStatus = new TaskStatus();
        savedStatus.setId(3L);
        savedStatus.setStatus("IN_PROGRESS");

        TaskStatusDTO expectedDTO = new TaskStatusDTO(3L, "IN_PROGRESS");

        when(taskStatusMapper.toEntity(inputDTO)).thenReturn(inputStatus);
        when(taskStatusService.createTaskStatus(inputStatus)).thenReturn(Optional.of(savedStatus));
        when(taskStatusMapper.toDTO(savedStatus)).thenReturn(expectedDTO);

        TaskStatusDTO result = taskStatusFacade.createTaskStatus(inputDTO);

        assertNotNull(result);
        assertEquals(3L, result.id());
        assertEquals("IN_PROGRESS", result.status());
        verify(taskStatusService, times(1)).createTaskStatus(inputStatus);
        verify(taskStatusMapper, times(1)).toEntity(inputDTO);
        verify(taskStatusMapper, times(1)).toDTO(savedStatus);
    }

    @Test
    void shouldReturnAllStatuses() {
        List<TaskStatus> statuses = Arrays.asList(taskStatus1, taskStatus2);
        List<TaskStatusDTO> expectedDTOs = Arrays.asList(taskStatusDTO1, taskStatusDTO2);

        when(taskStatusService.getTaskStatus()).thenReturn(Optional.of(statuses));
        when(taskStatusMapper.toDTOList(Optional.of(statuses))).thenReturn(expectedDTOs);

        List<TaskStatusDTO> result = taskStatusFacade.getTaskStatus();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(taskStatusService, times(1)).getTaskStatus();
        verify(taskStatusMapper, times(1)).toDTOList(Optional.of(statuses));
    }

    @Test
    void shouldReturnEmptyList_whenNoStatuses() {
        when(taskStatusService.getTaskStatus()).thenReturn(Optional.of(Collections.emptyList()));
        when(taskStatusMapper.toDTOList(Optional.of(Collections.emptyList()))).thenReturn(Collections.emptyList());

        List<TaskStatusDTO> result = taskStatusFacade.getTaskStatus();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(taskStatusService, times(1)).getTaskStatus();
    }

}
