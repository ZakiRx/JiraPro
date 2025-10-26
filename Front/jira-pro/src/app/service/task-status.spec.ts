import { describe, it, expect, beforeEach, afterEach } from 'vitest';
import { TestBed } from '@angular/core/testing';
import { TaskStatusService } from './task-status';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import {provideZonelessChangeDetection} from '@angular/core';



describe('TaskStatusService', () => {
  let service: TaskStatusService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        TaskStatusService,
        provideHttpClient(),
        provideHttpClientTesting(),
        provideZonelessChangeDetection(),
      ],
    });

    service = TestBed.inject(TaskStatusService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  function satisfyInitialLoad(data: TaskStatusModel[] = []) {
    const req = httpMock.expectOne('/api/task-statuses');
    expect(req.request.method).toBe('GET');
    req.flush(data);
  }

  it('should be created', () => {
    satisfyInitialLoad([]);
    expect(service).toBeTruthy();
  });

  it('loads task statuses on construction', () => {
    const mock: TaskStatusModel[] = [{ id: 1, status: 'Todo' }];
    satisfyInitialLoad(mock);

    service.getTaskStatuses().subscribe(statuses => {
      expect(statuses).toEqual(mock);
    });
  });

  it('createTaskStatus  then reloads', () => {
    satisfyInitialLoad([{ id: 1, status: 'Todo' }]);

    const created: TaskStatusModel = { id: 2, status: 'In Progress' };

    service.createTaskStatus(created).subscribe(resp => {
      expect(resp).toEqual(created);
    });

    const post = httpMock.expectOne('/api/task-statuses/create');
    expect(post.request.method).toBe('POST');
    expect(post.request.body).toEqual(created);
    post.flush(created);

    const reload = httpMock.expectOne('/api/task-statuses');
    expect(reload.request.method).toBe('GET');
    reload.flush([created]);
  });
});
