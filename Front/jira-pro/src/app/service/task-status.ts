import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable, tap} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TaskStatusService {
  private  taskStatusSubject = new BehaviorSubject<TaskStatusModel[]>([]);
  private taskStatus$ = this.taskStatusSubject.asObservable()
  private  httpClient: HttpClient = inject(HttpClient);

  constructor() {
    this.loadTaskStatus()
  }
  private loadTaskStatus(){
    return this.httpClient.get<TaskStatusModel[]>("/api/task-statuses").subscribe((taskStatuses)=>{
      this.taskStatusSubject.next(taskStatuses)
    });
  }

  public getTaskStatuses():Observable<TaskStatusModel[]>{
    return this.taskStatus$;
  }
  public createTaskStatus(task:TaskStatusModel):Observable<TaskStatusModel>{
    return this.httpClient.post<TaskStatusModel>("/api/task-statuses/create", task).pipe(tap(()=>this.loadTaskStatus()));
  }

}
