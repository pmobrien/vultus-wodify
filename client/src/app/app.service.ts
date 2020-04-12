import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/index';

@Injectable()
export class AppService {

  constructor(private http: HttpClient) {}

  getHelloWorld(): Observable<HelloWorld> {
    return this.http.get<HelloWorld>('/api/hello-world');
  }

  getAllAthletes(): Observable<Athlete[]> {
    return this.http.get<Athlete[]>('/api/athletes');
  }

  getAllPerformances(): Observable<Performance[]> {
    return this.http.get<Performance[]>('/api/performances');
  }
}

export interface HelloWorld {
  hello: string;
  world: string;
}

export interface Athlete {
  uuid: string;
  wodifyId: string;
  name: string;
  performance: Performance;
}

export interface Performance {
  uuid: string;
  pr: boolean;
  comment: string;
  result: string;
  type: string;
  date: Date;
  athlete: Athlete;
}
