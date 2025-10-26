import { describe, it, expect, beforeEach, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideZonelessChangeDetection } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TaskBoard } from './task-board';
import { TaskService } from '../../service/task-service';
import { TaskStatusService } from '../../service/task-status';
import { MatDialog } from '@angular/material/dialog';
import { of } from 'rxjs';


describe('TaskFilter', () => {
  let component: TaskBoard;
  let fixture: ComponentFixture<TaskBoard>;
  let mockTaskService: any;
  let mockTaskStatusService: any;
  let mockDialog: any;
  let mockDialogRef: any;

  const mockStatuses: TaskStatusModel[] = [
    { id: 1, status: 'To Do' },
    { id: 2, status: 'In Progress' }
  ];

  const mockTasks: TaskModel[] = [
    { id: 1, label: 'Task 1', description: 'Desc 1', completed: { id: 1, status: 'To Do' } },
    { id: 2, label: 'Task 2', description: 'Desc 2', completed: { id: 2, status: 'In Progress' } }
  ];

  beforeEach(async () => {
    mockTaskService = {
      getTasks: vi.fn().mockReturnValue(of(mockTasks)),
      createTask: vi.fn().mockReturnValue(of({})),
      updateTask: vi.fn().mockReturnValue(of({}))
    };

    mockTaskStatusService = {
      getTaskStatuses: vi.fn().mockReturnValue(of(mockStatuses))
    };

    mockDialogRef = {
      afterClosed: vi.fn().mockReturnValue(of(null))
    };

    mockDialog = {
      open: vi.fn().mockReturnValue(mockDialogRef)
    };

    await TestBed.configureTestingModule({
      imports: [TaskBoard],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideZonelessChangeDetection(),
        { provide: TaskService, useValue: mockTaskService },
        { provide: TaskStatusService, useValue: mockTaskStatusService },
        { provide: MatDialog, useValue: mockDialog },
        MatSnackBar
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TaskBoard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load tasks and statuses on init', () => {
    expect(mockTaskService.getTasks).toHaveBeenCalled();
    expect(mockTaskStatusService.getTaskStatuses).toHaveBeenCalled();
    expect(component.totalTasks()).toBe(2);
  });

  it('should open dialog and create task when submitted', () => {
    const newTask: TaskModel = { id: 3, label: 'New Task', description: 'New Desc', completed: { id: 1, status: 'To Do' } };
    mockDialogRef.afterClosed.mockReturnValue(of(newTask));
    mockTaskService.createTask.mockReturnValue(of(newTask));

    component.openDialog();

    expect(mockDialog.open).toHaveBeenCalled();
    expect(mockTaskService.createTask).toHaveBeenCalledWith(newTask);
  });

  it('should cleanup subscriptions on destroy', () => {
    const unsubscribeSpy = vi.spyOn(component['subscriptions'], 'unsubscribe');

    component.ngOnDestroy();

    expect(unsubscribeSpy).toHaveBeenCalled();
  });
});
