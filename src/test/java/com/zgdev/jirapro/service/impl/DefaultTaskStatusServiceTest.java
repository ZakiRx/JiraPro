package com.zgdev.jirapro.service.impl;

import com.zgdev.jirapro.entity.TaskStatus;
import com.zgdev.jirapro.repository.TaskStatusRepository;
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
class DefaultTaskStatusServiceTest {

    @Mock
    private TaskStatusRepository taskStatusRepository;

    @InjectMocks
    private DefaultTaskStatusService taskStatusService;

    private TaskStatus taskStatus1;
    private TaskStatus taskStatus2;

    @BeforeEach
    void setUp() {
        taskStatus1 = new TaskStatus();
        taskStatus1.setId(1L);
        taskStatus1.setStatus("TO_DO");

        taskStatus2 = new TaskStatus();
        taskStatus2.setId(2L);
        taskStatus2.setStatus("DONE");
    }

    @Test
    void shouldReturnAllStatuses() {
        List<TaskStatus> statuses = Arrays.asList(taskStatus1, taskStatus2);
        when(taskStatusRepository.findAll()).thenReturn(statuses);

        Optional<List<TaskStatus>> result = taskStatusService.getTaskStatus();

        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        verify(taskStatusRepository, times(1)).findAll();
    }



    @Test
    void shouldReturnStatus_whenValidStatusName() {
        when(taskStatusRepository.getTaskStatusByStatus("TO_DO")).thenReturn(Optional.of(taskStatus1));

        Optional<TaskStatus> result = taskStatusService.getTaskStatusByStatus("TO_DO");

        assertTrue(result.isPresent());
        assertEquals("TO_DO", result.get().getStatus());
        verify(taskStatusRepository, times(1)).getTaskStatusByStatus("TO_DO");
    }

    @Test
    void shouldReturnEmpty_whenInvalidStatusName() {
        when(taskStatusRepository.getTaskStatusByStatus("INVALID")).thenReturn(Optional.empty());

        Optional<TaskStatus> result = taskStatusService.getTaskStatusByStatus("INVALID");

        assertFalse(result.isPresent());
        verify(taskStatusRepository, times(1)).getTaskStatusByStatus("INVALID");
    }

    @Test
    void shouldReturnStatus_whenValidId() {
        when(taskStatusRepository.findById(1L)).thenReturn(Optional.of(taskStatus1));

        Optional<TaskStatus> result = taskStatusService.getTaskStatusById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("TO_DO", result.get().getStatus());
        verify(taskStatusRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnEmpty_whenInvalidId() {
        when(taskStatusRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<TaskStatus> result = taskStatusService.getTaskStatusById(999L);

        assertFalse(result.isPresent());
        verify(taskStatusRepository, times(1)).findById(999L);
    }

    @Test
    void shouldCreateTaskStatus() {
        when(taskStatusRepository.save(any(TaskStatus.class))).thenReturn(taskStatus1);

        Optional<TaskStatus> result = taskStatusService.createTaskStatus(taskStatus1);

        assertTrue(result.isPresent());
        assertEquals("TO_DO", result.get().getStatus());
        verify(taskStatusRepository, times(1)).save(taskStatus1);
    }

    @Test
    void shouldReturnEmpty_whenRemovingStatus() {
        Optional<TaskStatus> result = taskStatusService.removeTaskStatus(taskStatus1);

        assertFalse(result.isPresent());
        verifyNoInteractions(taskStatusRepository);
    }

    @Test
    void shouldReturnEmpty_whenUpdatingStatus() {
        Optional<TaskStatus> result = taskStatusService.updateTaskStatus(1);

        assertFalse(result.isPresent());
        verifyNoInteractions(taskStatusRepository);
    }
}