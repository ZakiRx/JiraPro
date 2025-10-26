import { describe, it, expect, beforeEach, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TaskStatusForm } from './task-status-form';
import { MatDialogRef } from '@angular/material/dialog';
import { ReactiveFormsModule } from '@angular/forms';
import { provideZonelessChangeDetection } from '@angular/core';

describe('TaskStatusForm', () => {
  let component: TaskStatusForm;
  let fixture: ComponentFixture<TaskStatusForm>;
  let mockDialogRef: any;

  beforeEach(async () => {
    mockDialogRef = {
      close: vi.fn()
    };

    await TestBed.configureTestingModule({
      imports: [TaskStatusForm, ReactiveFormsModule],
      providers: [
        provideZonelessChangeDetection(),
        { provide: MatDialogRef, useValue: mockDialogRef }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TaskStatusForm);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should validate status field correctly', () => {
    const status = component.status;

    status?.setValue('');
    expect(status?.hasError('required')).toBeTruthy();


    status?.setValue('a');
    expect(status?.hasError('minlength')).toBeTruthy();

    status?.setValue('Done');
    expect(status?.valid).toBeTruthy();
  });

  it('should submit valid form and close dialog', () => {
    component.statusForm.patchValue({ status: 'In Progress' });

    component.submit();

    expect(mockDialogRef.close).toHaveBeenCalledWith({ status: 'In Progress' });
  });

  it('should close dialog on cancel', () => {
    component.cancel();

    expect(mockDialogRef.close).toHaveBeenCalled();
  });
});
