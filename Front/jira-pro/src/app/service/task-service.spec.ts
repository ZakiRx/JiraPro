import { describe, it, expect, beforeEach, afterEach } from 'vitest';
import { TestBed } from '@angular/core/testing';
import { TaskService } from './task-service';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import {provideZonelessChangeDetection} from '@angular/core';

interface TaskModel {
  id: number;
  label: string;
  description?: string;
}

describe('TaskService', () => {
  let service: TaskService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        TaskService,
        provideHttpClient(),
        provideHttpClientTesting(),
        provideZonelessChangeDetection()
      ],
    });

    service = TestBed.inject(TaskService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  function satisfyInitialLoad(data: TaskModel[] = []) {
    const req = httpMock.expectOne('/api/tasks');
    expect(req.request.method).toBe('GET');
    req.flush(data);
  }

  it('should be created', () => {
    satisfyInitialLoad([]);
    expect(service).toBeTruthy();
  });

  it('loads tasks on construction', () => {
    const mock: TaskModel[] = [{ id: 1, label: 'A' }];
    satisfyInitialLoad(mock);

    service.getTasks().subscribe(tasks => {
      expect(tasks).toEqual(mock);
    });
  });

  it('createTask and reloads', () => {
    satisfyInitialLoad([]);

    const created: TaskModel = { id: 10, label: 'New' };

    service.createTask(created).subscribe(resp => {
      expect(resp).toEqual(created);
    });

    const post = httpMock.expectOne('/api/tasks/create');
    expect(post.request.method).toBe('POST');
    expect(post.request.body).toEqual(created);
    post.flush(created);

    const reload = httpMock.expectOne('/api/tasks');
    expect(reload.request.method).toBe('GET');
    reload.flush([created]);
  });

  it('removeTask and reload list tasks', () => {
    satisfyInitialLoad([{ id: 1, label: 'A' }]);

    const t: TaskModel = { id: 2, label: 'Del' };

    service.removeTask(t).subscribe();

    const del = httpMock.expectOne(`/api/tasks/delete/${t.id}`);
    expect(del.request.method).toBe('DELETE');
    del.flush({});

    const reload = httpMock.expectOne('/api/tasks');
    expect(reload.request.method).toBe('GET');
    reload.flush([]);
  });

  it('updateTask and  reloads', () => {
    satisfyInitialLoad([{ id: 1, label: 'A' }]);

    const t: TaskModel = { id: 3, label: 'Upd' };

    service.updateTask(t).subscribe(resp => {
      expect(resp).toEqual(t);
    });

    const put = httpMock.expectOne(`/api/tasks/update/${t.id}`);
    expect(put.request.method).toBe('PUT');
    expect(put.request.body).toEqual(t);
    put.flush(t);

    const reload = httpMock.expectOne('/api/tasks');
    expect(reload.request.method).toBe('GET');
    reload.flush([t]);
  });
});
