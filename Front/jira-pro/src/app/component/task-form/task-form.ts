import {ChangeDetectionStrategy, Component, effect, inject, OnInit, output, signal} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatError, MatFormField} from '@angular/material/form-field';
import {MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatButton, MatMiniFabButton} from '@angular/material/button';
import {
  MAT_DIALOG_DATA, MatDialog,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent, MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import {TaskCard} from '../task-card/task-card';
import {MatOption, MatSelect} from '@angular/material/select';
import {TaskStatusService} from '../../service/task-status';
import {Observable} from 'rxjs';
import {AsyncPipe} from '@angular/common';
import {MatIcon} from '@angular/material/icon';
import {TaskStatusForm} from '../task-status-form/task-status-form';
import {MatTooltip} from '@angular/material/tooltip';
import {showError} from '../../util/error-dialog';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-task-form',
  imports: [
    MatError,
    MatLabel,
    MatFormField,
    ReactiveFormsModule,
    MatInput,
    MatButton,
    MatDialogContent,
    MatDialogClose,
    MatDialogActions,
    MatDialogTitle,
    MatSelect,
    MatOption,
    AsyncPipe,
    MatIcon,
    MatMiniFabButton,
    MatTooltip
  ],
  templateUrl: './task-form.html',
  styleUrl: './task-form.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TaskForm implements OnInit {
  protected data = inject(MAT_DIALOG_DATA);
  private formBuilder = inject(FormBuilder);
  private dialogRef = inject(MatDialogRef<TaskCard>)
  private taskStatusService = inject(TaskStatusService);
  private readonly dialog = inject(MatDialog);
  private readonly snackBar = inject(MatSnackBar);
  task = signal<TaskModel | null>(null);


  onSubmit = output<TaskModel>();
  onCancel = output<void>();

  taskForm!: FormGroup;
  taskStatuses$: Observable<TaskStatusModel[]>;

  constructor() {
    this.task.set(this.data?.task);
    this.taskStatuses$ = this.taskStatusService.getTaskStatuses();
    effect(() => {
      const taskData = this.task();
      if (taskData && this.taskForm) {
        this.taskForm.patchValue({
          label: taskData.label,
          description: taskData.description,
          completed: taskData.completed
        });
      }
    });
  }

  ngOnInit(): void {

    this.taskForm = this.formBuilder.group({
      label: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      completed: ['', Validators.required]
    });
  }

  submitForm(): void {
    if (this.taskForm.valid) {
      const formValue = this.taskForm.value;

      const taskData: TaskModel = {
        id: this.task()?.id,
        label: formValue.label,
        description: formValue.description,
        completed: this.task() ? this.task()?.completed : {status: formValue.completed},
      };
      this.dialogRef.close(taskData);
      this.taskForm.reset();
    } else {
      Object.keys(this.taskForm.controls).forEach(key => {
        this.taskForm.get(key)?.markAsTouched();
      });
    }
  }

  openCreateStatusDialog(): void {
    const dialogRef = this.dialog.open(TaskStatusForm, {
      width: '500px',
      disableClose: false
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const newStatus: TaskStatusModel = {
          status: result.status as string,
        };

        this.taskStatusService.createTaskStatus(newStatus).subscribe({
          next: (createdStatus) => {
            console.log('Status created:', createdStatus);
            this.taskForm.patchValue({completed: createdStatus.id});
          },
          error: (error) => {
            showError(error, 'Error creating status', this.snackBar);
          }
        });
      }
    });
  }

  cancel(): void {
    this.taskForm.reset();
    this.dialogRef.close();
  }

  get label() {
    return this.taskForm.get('label');
  }

  get description() {
    return this.taskForm.get('description');
  }

  get completed() {
    return this.taskForm.get('completed');
  }
}
