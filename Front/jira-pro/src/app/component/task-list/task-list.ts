import {
  ChangeDetectionStrategy,
  Component,
  input,

} from '@angular/core';
import {TaskCard} from '../task-card/task-card';
import {CdkDrag} from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-task-list',
  imports: [
    TaskCard,
    CdkDrag
  ],
  templateUrl: './task-list.html',
  styleUrl: './task-list.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TaskList {
   tasks = input<TaskModel[]>([]);

}
