import { Component } from '@angular/core';
import { AppService } from './app.service';
import { Performance, Workout } from './app.service';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  dtOptions: DataTables.Settings = {};

  workouts: Workout[] = [];
  performances: Performance[] = [];

  dtTrigger: Subject<any> = new Subject();

  constructor(private service: AppService) {}

  ngOnInit(): void {
    this.dtOptions = {
      pagingType: 'full_numbers',
      pageLength: 25
    };

    this.service.getAllWorkouts().subscribe(workouts => {
      this.workouts = workouts;
    });

    this.service.getAllPerformances().subscribe(performances => {
      this.performances = performances;
      this.dtTrigger.next();
    });
  }

  getPerformanceResult(performance: Performance) {
    let result = performance.result;

    if(performance.type === 'SCALED') {
      result += ' (Scaled)';
    } else if(performance.type === 'RX') {
      result += ' (Rx)';
    } else if(performance.type === 'RX_PLUS') {
      result += ' (Rx+)';
    }

    return result;
  }
}
