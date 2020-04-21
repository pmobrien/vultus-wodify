import { Component, ViewChild } from '@angular/core';
import { DataTableDirective } from 'angular-datatables';
import { AppService } from './app.service';
import { Athlete, Performance, Workout } from './app.service';
import { Observable, Subject, empty } from 'rxjs';
import { map } from 'rxjs/operators';

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

  athletes: Observable<Athlete[]> = empty();
  workouts: Observable<Workout[]> = empty();
  performances: Performance[] = [];

  selectedAthleteUuid: string;
  selectedWorkoutUuid: string;

  view: ViewType = ViewType.athlete;

  dtTrigger: Subject<any> = new Subject();

  constructor(private service: AppService) {}

  ngOnInit(): void {
    this.dtOptions = {
      lengthChange: false,
      searching: false,
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
      this.version = version.version;
    });

    if(this.view === ViewType.athlete) {
      this.fetchAthletes();
    } else if(this.view === ViewType.leaderboard) {
      this.fetchWorkouts();
    }
  }

  onAthleteChange($event): void {
    this.doOnAthleteChange($event.uuid);
  }

  doOnAthleteChange(athleteUuid: string) {
    this.fetchWorkouts(athleteUuid);
  }

  onWorkoutChange($event): void {
    this.doOnWorkoutChange($event.uuid);
  }

  doOnWorkoutChange(workoutUuid: string): void {
    if(this.view === ViewType.athlete) {
      this.getPerformancesByWorkoutId(workoutUuid, this.selectedAthleteUuid);
    } else {
      this.getPerformancesByWorkoutId(workoutUuid);
    }
  }

  fetchAthletes(): void {
    this.athletes = this.service.getAllAthletes()
      .pipe(
        map(athletes => {
          athletes.sort((one: Workout, two: Workout) => {
            if(one.name < two.name) {
              return -1;
            } else if(one.name > two.name) {
              return 1;
            } else {
              return 0;
            }
          });

          if(!this.selectedAthleteUuid) {
            this.selectedAthleteUuid = athletes[0].uuid;
          }
          
          this.doOnAthleteChange(this.selectedAthleteUuid);

          return athletes;
        })
      );
  }

  fetchWorkouts(athleteUuid?: string): void {
    this.workouts = this.service.getAllWorkouts(athleteUuid)
      .pipe(
        map(workouts => {
          workouts.sort((one: Workout, two: Workout) => {
            if(one.name < two.name) {
              return -1;
            } else if(one.name > two.name) {
              return 1;
            } else {
              return 0;
            }
          });

          let found: boolean = false;
          for(var i = 0; i < workouts.length; ++i) {
            if(workouts[i].uuid === this.selectedWorkoutUuid) {
              found = true;
            }
          }

          if(!this.selectedWorkoutUuid || !found) {
            this.selectedWorkoutUuid = workouts[0].uuid;
          }
          
          this.doOnWorkoutChange(this.selectedWorkoutUuid);
      
          return workouts;
        })
      );
  }

  getPerformancesByWorkoutId(workoutUuid: string, athleteUuid?: string) {
    this.service.getPerformancesByWorkoutUuid(workoutUuid, athleteUuid).subscribe(performances => {
      // need to add in a "sort hack"
      // the table is not properly taking in the full object in the columnDefs object
      // so we have to build a sort value and append the display value to the end
      // format is 'sortValue|displayValue'
      performances.map(p => {
        let val = 0;

        if(p.workout.type === 'AMRAP_REPS') {
          // format is 'XXX Reps'
          // just take the XXX as the sort value
          val = Number(p.result.split(' ')[0]);
        } else if(p.workout.type === 'AMRAP_ROUNDS_AND_REPS') {
          // format is X + Y
          // multiply X by 1000 and add Y for sort value
          // Y will never be over 1000 so somehing like 5 + 99 won't end up being more than 6 + 2
          let split = p.result.split('+')
          val = (Number(split[0]) * 1000) + Number(split[1]);
        } else if(p.workout.type === 'EACH_ROUND_FOR_REPS') {
          // format is 'XXX Total Reps'
          // just take the XXX as the sort value
          val = Number(p.result.split(' ')[0]);
        } else if(p.workout.type === 'EACH_ROUND_FOR_TIME') {
          // format is 'XX:XX' (minutes:seconds)
          // just convert to seconds for sort value
          let split = p.result.split(':')
          val = ((Number(split[0]) * 60) + Number(split[1]));
        } else if(p.workout.type === 'TIME') {
          // format is 'XX:XX' (minutes:seconds)
          // just convert to seconds for sort value
          let split = p.result.split(':')
          val = ((Number(split[0]) * 60) + Number(split[1]));
        } else if(p.workout.type === 'WEIGHT') {
          // format is 'XXX lbs'
          // just take XXX as sort value
          val = Number(p.result.split(' ')[0]);
        }

        p.sortHack = val + '|' + p.result;
        
        // TODO adjust value for rx/rx+

        if(p.type === 'RX') {
          p.sortHack += ' (Rx)';
        } else if(p.type === 'RX_PLUS') {
          p.sortHack += ' (Rx+)';
        }
      });

      this.performances = performances;
      this.rerender();
    });
  }

  onViewTypeClick($event): void {
    if($event.target.id === 'athleteButton') {
      this.view = ViewType.athlete;
      this.fetchAthletes();
    } else if($event.target.id === 'leaderboardButton') {
      this.view = ViewType.leaderboard;
      this.fetchWorkouts();
    } 
  }

  isAthleteView(): boolean {
    return this.view === ViewType.athlete;
  }

  isLeaderboardView(): boolean {
    return this.view === ViewType.leaderboard;
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

enum ViewType {
  athlete,
  leaderboard
}
