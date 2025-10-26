package com.zgdev.jirapro.facade;

import com.zgdev.jirapro.dto.TaskDTO;
import com.zgdev.jirapro.dto.TaskStatusDTO;
import com.zgdev.jirapro.entity.Task;
import com.zgdev.jirapro.entity.TaskStatus;
import com.zgdev.jirapro.exception.TaskNotFoundException;
import com.zgdev.jirapro.mapper.TaskMapper;
import com.zgdev.jirapro.service.TaskService;
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
class DefaultTaskFacadeTest {

    @Mock
    private TaskService taskService;

    @Mock
    private TaskStatusService taskStatusService;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private DefaultTaskFacade taskFacade;

    private Task task1;
    private Task task2;
    private TaskDTO taskDTO1;
    private TaskDTO taskDTO2;
    private TaskStatus taskStatus;
    private TaskStatusDTO taskStatusDTO;

    @BeforeEach
    void setUp() {
        taskStatus = new TaskStatus();
        taskStatus.setId(1L);
        taskStatus.setStatus("TO_DO");

        taskStatusDTO = new TaskStatusDTO(1L, "TO_DO");

        task1 = new Task();
        task1.setId(1L);
        task1.setLabel("Task 1");
        task1.setCompleted(taskStatus);

        task2 = new Task();
        task2.setId(2L);
        task2.setLabel("Task 2");
        task2.setCompleted(taskStatus);

        taskDTO1 = new TaskDTO(1L, "Task 1","Description 1", taskStatusDTO);
        taskDTO2 = new TaskDTO(2L, "Task 2", "Description 2", taskStatusDTO);
    }

    @Test
    void shouldReturnAllTasks() {
        List<Task> tasks = Arrays.asList(task1, task2);
        List<TaskDTO> expectedDTOs = Arrays.asList(taskDTO1, taskDTO2);

        when(taskService.getTasks()).thenReturn(Optional.of(tasks));
        when(taskMapper.toDTOList(tasks)).thenReturn(expectedDTOs);

        List<TaskDTO> result = taskFacade.getTasks();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(taskService, times(1)).getTasks();
        verify(taskMapper, times(1)).toDTOList(tasks);
    }

    @Test
    void shouldReturnEmptyList_whenNoTasks() {
        List<Task> emptyList = Collections.emptyList();
        List<TaskDTO> emptyDTOList = Collections.emptyList();

        when(taskService.getTasks()).thenReturn(Optional.of(emptyList));
        when(taskMapper.toDTOList(emptyList)).thenReturn(emptyDTOList);

        List<TaskDTO> result = taskFacade.getTasks();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(taskService, times(1)).getTasks();
    }



    @Test
    void shouldReturnTask_whenValidId() {
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task1));
        when(taskMapper.toDTO(task1)).thenReturn(taskDTO1);

        TaskDTO result = taskFacade.getTaskById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Task 1", result.label());
        verify(taskService, times(1)).getTaskById(1L);
        verify(taskMapper, times(1)).toDTO(task1);
    }

    @Test
    void shouldThrowException_whenTaskNotFound() {
        when(taskService.getTaskById(999L)).thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskFacade.getTaskById(999L)
        );

        assertTrue(exception.getMessage().contains("999"));
        verify(taskService, times(1)).getTaskById(999L);
    }

    @Test
    void shouldCreateTask_withValidData() {
        TaskDTO inputDTO = new TaskDTO(1,"New Task","New Description",taskStatusDTO);
        Task inputTask = new Task();
        inputTask.setLabel("New Task");

        when(taskMapper.toEntity(inputDTO)).thenReturn(inputTask);
        when(taskStatusService.getTaskStatusByStatus("TO_DO")).thenReturn(Optional.of(taskStatus));
        when(taskService.createTaskWithStatus(inputTask, taskStatus)).thenReturn(Optional.of(task1));
        when(taskMapper.toDTO(task1)).thenReturn(taskDTO1);

        TaskDTO result = taskFacade.createTask(inputDTO);

        assertNotNull(result);
        verify(taskMapper, times(1)).toEntity(inputDTO);
        verify(taskStatusService, times(1)).getTaskStatusByStatus("TO_DO");
        verify(taskService, times(1)).createTaskWithStatus(inputTask, taskStatus);
        verify(taskMapper, times(1)).toDTO(task1);
    }

    @Test
    void shouldThrowException_whenStatusNotFound() {
        TaskDTO inputDTO = new TaskDTO(1,"New Task","New Description",taskStatusDTO);
        Task inputTask = new Task();

        when(taskMapper.toEntity(inputDTO)).thenReturn(inputTask);
        when(taskStatusService.getTaskStatusByStatus("TO_DO")).thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskFacade.createTask(inputDTO)
        );

        assertTrue(exception.getMessage().contains("TO_DO"));
        verify(taskService, never()).createTaskWithStatus(any(), any());
    }

    @Test
    void shouldUpdateTaskStatus() {
        TaskStatusDTO newStatusDTO = new TaskStatusDTO(2L, "DONE");
        TaskDTO updateDTO = new TaskDTO(1L, "Task 1","Description",newStatusDTO);

        TaskStatus completedStatus = new TaskStatus();
        completedStatus.setId(2L);
        completedStatus.setStatus("DONE");

        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task1));
        when(taskStatusService.getTaskStatusByStatus("DONE"))
                .thenReturn(Optional.of(completedStatus));
        when(taskService.updateTaskStatus(task1, completedStatus)).thenReturn(Optional.of(task1));
        when(taskMapper.toDTO(task1)).thenReturn(updateDTO);

        TaskDTO result = taskFacade.updateStatusOfTask(updateDTO);

        assertNotNull(result);
        verify(taskService, times(1)).getTaskById(1L);
        verify(taskStatusService, times(1)).getTaskStatusByStatus("DONE");
        verify(taskService, times(1)).updateTaskStatus(task1, completedStatus);
        verify(taskMapper, times(1)).toDTO(task1);
    }

    @Test
    void shouldThrowException_whenUpdatingNonExistentTask() {
        TaskDTO updateDTO = new TaskDTO(999L, "Task","Description",taskStatusDTO);
        when(taskService.getTaskById(999L)).thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskFacade.updateStatusOfTask(updateDTO)
        );

        assertTrue(exception.getMessage().contains("999"));
        verify(taskService, never()).updateTask(any());
    }

    @Test
    void shouldThrowException_whenInvalidStatus() {
        TaskStatusDTO invalidStatusDTO = new TaskStatusDTO(99L, "INVALID");
        TaskDTO updateDTO = new TaskDTO(1L, "Task 1","desqcription", invalidStatusDTO);

        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task1));
        when(taskStatusService.getTaskStatusByStatus("INVALID")).thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskFacade.updateStatusOfTask(updateDTO)
        );

        assertTrue(exception.getMessage().contains("INVALID"));
        verify(taskService, never()).updateTask(any());
    }

    @Test
    void shouldDeleteTask_whenExists() {
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task1));
        doNothing().when(taskService).removeTask(task1);

        assertDoesNotThrow(() -> taskFacade.deleteTask(1L));

        verify(taskService, times(1)).getTaskById(1L);
        verify(taskService, times(1)).removeTask(task1);
    }

    @Test
    void shouldThrowException_whenDeletingNonExistentTask() {
        when(taskService.getTaskById(999L)).thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskFacade.deleteTask(999L)
        );

        assertTrue(exception.getMessage().contains("999"));
        verify(taskService, times(1)).getTaskById(999L);
        verify(taskService, never()).removeTask(any());
    }

    @Test
    void shouldPropagateException_whenDeleteFails() {
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task1));
        doThrow(new RuntimeException("Database error")).when(taskService).removeTask(task1);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> taskFacade.deleteTask(1L)
        );

        assertEquals("Database error", exception.getMessage());
        verify(taskService, times(1)).getTaskById(1L);
        verify(taskService, times(1)).removeTask(task1);
    }
}