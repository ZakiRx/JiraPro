import { describe, it, expect, beforeEach, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ReactiveFormsModule } from '@angular/forms';
import { provideZonelessChangeDetection } from '@angular/core';
import { TaskForm } from './task-form';


describe('TaskForm', () => {
  let component: TaskForm;
  let fixture: ComponentFixture<TaskForm>;
  let mockDialogRef: any;

  beforeEach(async () => {
    mockDialogRef = {
      close: vi.fn()
    };

    await TestBed.configureTestingModule({
      imports: [TaskForm, ReactiveFormsModule],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideZonelessChangeDetection(),
        { provide: MatDialogRef, useValue: mockDialogRef },
        { provide: MAT_DIALOG_DATA, useValue: {} }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TaskForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should validate form fields correctly', () => {
    const label = component.label;
    const description = component.description;

    expect(component.taskForm.valid).toBeFalsy();

    label?.setValue('Valid Task Name');
    description?.setValue('This is a valid description');
    component.completed?.setValue('1');

    expect(component.taskForm.valid).toBeTruthy();
  });

  it('should submit valid form and close dialog', () => {
    component.taskForm.patchValue({
      label: 'Test Task',
      description: 'Test Description for task',
      completed: '1'
    });

    component.submitForm();

    expect(mockDialogRef.close).toHaveBeenCalled();
  });

  it('should reset form and close dialog on cancel', () => {
    component.taskForm.patchValue({
      label: 'Test',
      description: 'Test Description',
      completed: '1'
    });

    component.cancel();

    expect(mockDialogRef.close).toHaveBeenCalled();
    expect(component.taskForm.value).toEqual({
      label: null,
      description: null,
      completed: null
    });
  });
});
