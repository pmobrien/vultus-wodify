import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { DataTableDirective } from 'angular-datatables';
import { AppService } from './app.service';
import { Performance, Workout } from './app.service';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  @ViewChild(DataTableDirective, {static: false})
  dtElement: DataTableDirective;

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

      this.service.getPerformancesByWorkoutUuid(workouts[0].uuid).subscribe(performances => {
        this.performances = performances;
        this.dtTrigger.next();
      });
    });
  }

  onWorkoutChange(uuid: string): void {
    this.service.getPerformancesByWorkoutUuid(uuid).subscribe(performances => {
      this.performances = performances;
      this.rerender();
    });
  }

  getPerformanceResult(performance: Performance): string {
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

  rerender(): void {
    this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
      dtInstance.destroy();
      this.dtTrigger.next();
    });
  }
}
