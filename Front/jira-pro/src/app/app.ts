import { Component, signal } from '@angular/core';
import {TaskBoard} from './component/task-board/task-board';

@Component({
  selector: 'app-root',
  imports: [TaskBoard],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('jira-pro');
}
