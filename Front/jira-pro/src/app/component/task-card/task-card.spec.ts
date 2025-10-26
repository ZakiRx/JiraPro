import { describe, it, expect, beforeEach, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatDialog } from '@angular/material/dialog';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideZonelessChangeDetection } from '@angular/core';
import { TaskService } from '../../service/task-service';
import { of } from 'rxjs';
import { TaskCard } from './task-card';

describe('TaskCard', () => {
  let component: TaskCard;
  let fixture: ComponentFixture<TaskCard>;
  let mockTaskService: any;
  let mockDialog: any;
  let mockDialogRef: any;

  const mockTask: TaskModel = {
    id: 1,
    label: 'Test Task',
    description: 'Test Description',
    completed: { id: 1, status: 'In Progress' }
  };

  beforeEach(async () => {
    mockTaskService = {
      updateTask: vi.fn().mockReturnValue(of({})),
      removeTask: vi.fn().mockReturnValue(of({}))
    };

    mockDialogRef = {
      afterClosed: vi.fn().mockReturnValue(of(null))
    };

    mockDialog = {
      open: vi.fn().mockReturnValue(mockDialogRef)
    };

    await TestBed.configureTestingModule({
      imports: [TaskCard],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideZonelessChangeDetection(),
        { provide: TaskService, useValue: mockTaskService },
        { provide: MatDialog, useValue: mockDialog }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TaskCard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should open dialog and update task on close', () => {
    const updatedTask: TaskModel = { ...mockTask, label: 'Updated Task' };
    mockDialogRef.afterClosed.mockReturnValue(of(updatedTask));
    mockTaskService.updateTask.mockReturnValue(of(updatedTask));

    component.openDialog();

    expect(mockDialog.open).toHaveBeenCalled();
    expect(mockTaskService.updateTask).toHaveBeenCalledWith(updatedTask);
  });

  it('should remove task when task is provided', () => {
    component.removeTask(mockTask);

    expect(mockTaskService.removeTask).toHaveBeenCalledWith(mockTask);
  });

  it('should not remove task when task is null', () => {
    component.removeTask(null);

    expect(mockTaskService.removeTask).not.toHaveBeenCalled();
  });
});
