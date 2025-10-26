import { describe, it, expect, beforeEach } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideZonelessChangeDetection } from '@angular/core';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TaskList } from './task-list';

describe('TaskList', () => {
  let component: TaskList;
  let fixture: ComponentFixture<TaskList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TaskList],
      providers: [
        provideZonelessChangeDetection(),
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TaskList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with empty tasks array', () => {
    expect(component.tasks()).toEqual([]);
  });

  it('should accept tasks input', () => {
    const mockTasks: TaskModel[] = [
      { id: 1, label: 'Task A', description: 'Description A' },
      { id: 2, label: 'Task B', description: 'Description B' }
    ];

    fixture.componentRef.setInput('tasks', mockTasks);
    fixture.detectChanges();

    expect(component.tasks()).toEqual(mockTasks);
    expect(component.tasks().length).toBe(2);
  });

  it('should update tasks when input changes', () => {
    const initialTasks: TaskModel[] = [
      { id: 1, label: 'Task 1' }
    ];

    fixture.componentRef.setInput('tasks', initialTasks);
    expect(component.tasks().length).toBe(1);

    const updatedTasks: TaskModel[] = [
      { id: 1, label: 'Task 1' },
      { id: 2, label: 'Task 2' },
      { id: 3, label: 'Task 3' }
    ];

    fixture.componentRef.setInput('tasks', updatedTasks);
    expect(component.tasks().length).toBe(3);
  });
});
