import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable, tap} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private tasksSubject = new BehaviorSubject<TaskModel[]>([]);
  public tasks$ = this.tasksSubject.asObservable();
  private  httpClient: HttpClient = inject(HttpClient);
  constructor() {
    this.loadTasks();
  }
  private loadTasks(): void {
    this.httpClient.get<TaskModel[]>('/api/tasks').subscribe(tasks => {
      this.tasksSubject.next(tasks);
    });
  }

  public getTasks():Observable<TaskModel[]>{
    return this.tasks$;
  }
  public createTask(task:TaskModel):Observable<TaskModel>{
    return this.httpClient.post<TaskModel>("/api/tasks/create", task).pipe(tap(() => this.loadTasks()));
  }

  public removeTask(task:TaskModel){
    return this.httpClient.delete("/api/tasks/delete/"+task.id).pipe(tap(() => this.loadTasks()));
  }
  public updateTask(task:TaskModel){
    return this.httpClient.put("/api/tasks/update/"+task.id, task).pipe(tap(() => this.loadTasks()));
  }

}
