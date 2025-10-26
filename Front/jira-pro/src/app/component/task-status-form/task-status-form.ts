import {ChangeDetectionStrategy, Component, inject} from '@angular/core';
import {MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle} from '@angular/material/dialog';
import {MatError, MatFormField} from '@angular/material/form-field';
import {MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-task-status-form',
  imports: [
    MatDialogContent,
    MatDialogTitle,
    MatFormField,
    MatLabel,
    MatError,
    MatInput,
    ReactiveFormsModule,
    MatDialogActions,
    MatButton
  ],
  templateUrl: './task-status-form.html',
  styleUrl: './task-status-form.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TaskStatusForm {
  private fb = inject(FormBuilder);
  private dialogRef = inject(MatDialogRef<TaskStatusForm>);
  statusForm: FormGroup;

  constructor() {
    this.statusForm = this.fb.group({
      status: ['', [Validators.required, Validators.minLength(2)]],
    });
  }

  submit() {
    if (this.statusForm.valid) {
      this.dialogRef.close(this.statusForm.value);
    }
  }

  cancel() {
    this.dialogRef.close();
  }

  get status() {
    return this.statusForm.get('status');
  }
}
