import { Component } from '@angular/core';
import { AppService } from './app.service';
import { Performance } from './app.service';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  dtOptions: DataTables.Settings = {};
  performances: Performance[] = [];

  dtTrigger: Subject<any> = new Subject();

  constructor(private service: AppService) {}

  ngOnInit(): void {
    this.dtOptions = {
      pagingType: 'full_numbers',
      pageLength: 25
    };

    this.service.getAllPerformances().subscribe(performances => {
      this.performances = performances;
      this.dtTrigger.next();
    });
  }
}
