import {ChangeDetectionStrategy, Component, computed, inject, OnDestroy, OnInit, signal} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {MatCard, MatCardActions, MatCardHeader, MatCardTitle} from '@angular/material/card';
import {MatSnackBar} from '@angular/material/snack-bar';
import {CdkDragDrop, CdkDropList, CdkDropListGroup, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import {MatButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';

import {TaskService} from '../../service/task-service';
import {TaskStatusService} from '../../service/task-status';
import {TaskList} from '../task-list/task-list';
import {combineLatestWith, Subscription} from 'rxjs';
import {TaskForm} from '../task-form/task-form';
import {showError} from '../../util/error-dialog';


@Component({
  selector: 'app-task-board',
  imports: [
    TaskList,
    CdkDropList,
    CdkDropListGroup,
    MatButton,
    MatIcon,
    MatCard,
    MatCardHeader,
    MatCardTitle,
    MatCardActions
  ],
  templateUrl: './task-board.html',
  styleUrl: './task-board.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TaskBoard implements OnInit, OnDestroy {
  private readonly dialog = inject(MatDialog);
  private readonly taskService: TaskService = inject(TaskService);
  private readonly taskStatusService: TaskStatusService =  inject(TaskStatusService);
  private readonly snackBar = inject(MatSnackBar);
  private readonly subscriptions = new Subscription();

  taskStatus = signal<TaskStatusModel[]>([]);
  tasks = signal<Map<TaskStatusModel,TaskModel[]>>(new Map());
  taskEntries = computed(() => Array.from(this.tasks().entries()));
  totalTasks = signal<number>(0)

  ngOnInit(): void {
    const sub = this.taskStatusService.getTaskStatuses()
      .pipe(
        combineLatestWith(this.taskService.getTasks())
      )
      .subscribe({
        next: ([taskStatuses, taskModel]) => {
          const tasksByStatus = new Map<TaskStatusModel, TaskModel[]>();
          this.totalTasks.set(taskModel.length)
          taskStatuses.forEach(ts => {
            tasksByStatus.set(ts, taskModel.filter(tm => tm?.completed?.id === ts.id));
          });

          this.tasks.set(tasksByStatus);
          this.taskStatus.set(taskStatuses);
        },
        error: (err) => showError(err, 'Failed to load tasks/statuses',this.snackBar)
      });

    this.subscriptions.add(sub);
  }

  openDialog() {
    const dialogRef = this.dialog.open(TaskForm);

    const sub = dialogRef.afterClosed().subscribe({
      next: (result) => {
        if(!!result){
          const updatedTask = result as TaskModel;
          const createSub = this.taskService.createTask(updatedTask).subscribe({
            next: (task)=>{
              console.log("create task", task);
            },
            error: (err) => showError(err, 'Failed to create task',this.snackBar)
          });
          this.subscriptions.add(createSub);
        }

      },
      error: (err) => showError(err, 'Dialog error',this.snackBar)
    });

    this.subscriptions.add(sub);
  }

  drop(event: CdkDragDrop<{tasks: any[], status: TaskStatusModel}>) {
    const previousData = event.previousContainer.data;
    const currentData = event.container.data;
    if (event.previousContainer === event.container) {
      moveItemInArray(currentData.tasks, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(
        previousData.tasks,
        currentData.tasks,
        event.previousIndex,
        event.currentIndex,
      );
      const movedTask = currentData.tasks[event.currentIndex];
      movedTask.completed = currentData.status;
      this.updateTaskStatus(movedTask);
    }
  }

  private updateTaskStatus(task:TaskModel) {
    const sub = this.taskService.updateTask(task).subscribe({
      next: (updatedTask)=>{
        console.log("Update task status",updatedTask);
      },
      error: (err) => showError(err, 'Failed to update task',this.snackBar)
    });
    this.subscriptions.add(sub);
  }



  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
