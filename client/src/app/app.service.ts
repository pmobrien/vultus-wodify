import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/index';

@Injectable()
export class AppService {

  constructor(private http: HttpClient) {}

  getAllAthletes(): Observable<Athlete[]> {
    return this.http.get<Athlete[]>('/api/athletes');
  }

  getAllPerformances(): Observable<Performance[]> {
    return this.http.get<Performance[]>('/api/performances');
  }

  getPerformancesByWorkoutUuid(uuid: string): Observable<Performance[]> {
    return this.http.get<Performance[]>('/api/workouts/' + uuid + '/performances');
  }

  getAllWorkouts(): Observable<Workout[]> {
    return this.http.get<Workout[]>('/api/workouts');
  }

  getVersionNumber(): Observable<Version> {
    return this.http.get<Version>('/api/version');
  }
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
  workout: Workout;
  sortHack: string;   // the most appropriate name...
}

export interface Workout {
  uuid: string;
  type?: string;
  name?: string;
  scheme?: string;
  description?: string;
}

export interface Version {
  version: string;
}
