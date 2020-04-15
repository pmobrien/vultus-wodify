import { Component, ViewChild } from '@angular/core';
import { DataTableDirective, DataTablesModule } from 'angular-datatables';
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
  
  version: string;
  workouts: Workout[] = [];
  performances: Performance[] = [];

  dtTrigger: Subject<any> = new Subject();

  constructor(private service: AppService) {}

  ngOnInit(): void {
    this.dtOptions = {
      lengthChange: false,
      columnDefs: [{
        targets: 2,
        type: 'numeric',
        defaultContent: '',
        render: {
          display: function(data, type, row, meta) {
            return data.split('|')[1];
          },
          sort: function(data, type, row, meta) {
            return Number(data.split('|')[0]);
          }
        }
      }],
      pagingType: 'full_numbers',
      pageLength: 15
    };

    this.service.getVersionNumber().subscribe(version => {
      this.version = '[v' + version.version + ']';
    });

    this.service.getAllWorkouts().subscribe(workouts => {
      this.workouts = workouts;

      this.workouts.sort((one: Workout, two: Workout) => {
        if(one.name < two.name) {
          return -1;
        } else if(one.name > two.name) {
          return 1;
        } else {
          return 0;
        }
      });

      this.getPerformancesByWorkoutId(workouts[0].uuid);
    });
  }

  onWorkoutChange(uuid: string): void {
    this.getPerformancesByWorkoutId(uuid);
  }

  getPerformancesByWorkoutId(uuid: string) {
    this.service.getPerformancesByWorkoutUuid(uuid).subscribe(performances => {
      // need to add in a "sort hack"
      // the table is not properly taking in the full object in the columnDefs object
      // so we have to build a sort value and append the display value to the end
      // format is 'sortValue|displayValue'
      performances.map(p => {
        if(p.workout.type === 'AMRAP_REPS') {
          // format is 'XXX Reps'
          // just take the XXX as the sort value
          p.sortHack = p.result.split(' ')[0] + '|' + p.result;
        } else if(p.workout.type === 'AMRAP_ROUNDS_AND_REPS') {
          // format is X + Y
          // multiply X by 1000 and add Y for sort value
          // Y will never be over 1000 so somehing like 5 + 99 won't end up being more than 6 + 2
          let split = p.result.split('+')
          p.sortHack = (Number(split[0]) * 1000) + Number(split[1]) + '|' + p.result;
        } else if(p.workout.type === 'EACH_ROUND_FOR_REPS') {
          // format is 'XXX Total Reps'
          // just take the XXX as the sort value
          p.sortHack = p.result.split(' ')[0] + '|' + p.result;
        } else if(p.workout.type === 'EACH_ROUND_FOR_TIME') {
          // format is 'XX:XX' (minutes:seconds)
          // just convert to seconds for sort value
          let split = p.result.split(':')
          p.sortHack = ((Number(split[0]) * 60) + Number(split[1])) + '|' + p.result;
        } else if(p.workout.type === 'TIME') {
          // format is 'XX:XX' (minutes:seconds)
          // just convert to seconds for sort value
          let split = p.result.split(':')
          p.sortHack = ((Number(split[0]) * 60) + Number(split[1])) + '|' + p.result;
        } else if(p.workout.type === 'WEIGHT') {
          // format is 'XXX lbs'
          // just take XXX as sort value
          p.sortHack = p.result.split(' ')[0] + '|' + p.result;
        }
      });

      this.performances = performances;
      this.rerender();
    });
  }

  getPerformanceResult(performance: Performance): string {
    let result = performance.result;

    if(performance.type === 'RX') {
      result += ' (Rx)';
    } else if(performance.type === 'RX_PLUS') {
      result += ' (Rx+)';
    }

    return result;
  }

  rerender(): void {
    if(this.dtElement.dtInstance) {
      this.dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
        dtInstance.destroy();
        this.dtTrigger.next();
      });
    } else {
      this.dtTrigger.next();
    }
  }
}
