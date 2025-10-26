package com.zgdev.jirapro.service.impl;

import com.zgdev.jirapro.entity.Task;
import com.zgdev.jirapro.entity.TaskStatus;
import com.zgdev.jirapro.repository.TaskRepository;
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
class DefaultTaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private DefaultTaskService taskService;

    private Task task1;
    private Task task2;
    private TaskStatus taskStatus;

    @BeforeEach
    void setUp() {
        taskStatus = new TaskStatus();
        taskStatus.setId(1L);
        taskStatus.setStatus("TO_DO");

        task1 = new Task();
        task1.setId(1L);
        task1.setLabel("Task 1");
        task1.setCompleted(taskStatus);

        task2 = new Task();
        task2.setId(2L);
        task2.setLabel("Task 2");
        task2.setCompleted(taskStatus);
    }

    @Test
    void shouldReturnTasks() {
        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskRepository.findAll()).thenReturn(tasks);

        Optional<List<Task>> result = taskService.getTasks();

        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyList_whenNoTasks() {
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        Optional<List<Task>> result = taskService.getTasks();

        assertTrue(result.isPresent());
        assertTrue(result.get().isEmpty());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnTask_whenValidId() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));

        Optional<Task> result = taskService.getTaskById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Task 1", result.get().getLabel());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnEmpty_whenInvalidId() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Task> result = taskService.getTaskById(999L);

        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).findById(999L);
    }

    @Test
    void shouldReturnTasks_whenStatusMatches() {
        when(taskRepository.getTasksByCompleted(taskStatus)).thenReturn(Optional.of(List.of(task1, task2)));

        Optional<List<Task>> result = taskService.getTasksByStatus(taskStatus);

        assertTrue(result.isPresent());
        assertEquals(taskStatus, result.get());
        verify(taskRepository, times(1)).getTasksByCompleted(taskStatus);
    }

    @Test
    void shouldReturnEmpty_whenStatusNotMatched() {
        when(taskRepository.getTasksByCompleted(taskStatus)).thenReturn(Optional.empty());

        Optional<List<Task>>  result = taskService.getTasksByStatus(taskStatus);

        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).getTasksByCompleted(taskStatus);
    }

    @Test
    void shouldCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task1);

        Optional<Task> result = taskService.createTask(task1);

        assertTrue(result.isPresent());
        assertEquals("Task 1", result.get().getLabel());
        verify(taskRepository, times(1)).save(task1);
    }

    @Test
    void shouldDeleteTask() {
        doNothing().when(taskRepository).delete(task1);

        assertDoesNotThrow(() -> taskService.removeTask(task1));

        verify(taskRepository, times(1)).delete(task1);
    }

    @Test
    void shouldUpdateTask() {
        task1.setLabel("Updated Task");
        when(taskRepository.save(task1)).thenReturn(task1);

        Optional<Task> result = taskService.updateTask(task1);

        assertTrue(result.isPresent());
        assertEquals("Updated Task", result.get().getLabel());
        verify(taskRepository, times(1)).save(task1);
    }
}