import {ChangeDetectionStrategy, Component, inject, input} from '@angular/core';
import {MatCard, MatCardContent, MatCardFooter, MatCardHeader, MatCardTitle} from '@angular/material/card';
import {MatChip, MatChipSet} from '@angular/material/chips';
import {MatFabButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {TaskForm} from '../task-form/task-form';
import {TaskService} from '../../service/task-service';
import {MatDialog} from '@angular/material/dialog';

@Component({
  selector: 'app-task-card',
  imports: [
    MatCard,
    MatCardHeader,
    MatCardTitle,
    MatCardFooter,
    MatChipSet,
    MatChip,
    MatCardContent,
    MatIcon,
    MatFabButton
  ],
  templateUrl: './task-card.html',
  styleUrl: './task-card.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TaskCard {
  private readonly dialog = inject(MatDialog);
  private readonly taskService: TaskService = inject(TaskService)
  taskDetail=  input<TaskModel|null>(null);
  openDialog() {
    const dialogRef = this.dialog.open(TaskForm,{
      data: {
        task: this.taskDetail()
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if(!!result){
        const updatedTask = result as TaskModel;
        this.taskService.updateTask(updatedTask).subscribe((task)=>{
          console.log("update task", task);
        });
      }

      console.log('Dialog result:',result);
    });
  }

  public removeTask(taskModel: TaskModel | null) {
    if(taskModel){
      this.taskService.removeTask(taskModel).subscribe((result) => {
        console.log("deleted task", result);
      })
    }
  }
}
