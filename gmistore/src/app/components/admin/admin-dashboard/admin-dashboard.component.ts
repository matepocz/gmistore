import {Component, OnInit, ViewChild} from '@angular/core';
import { map } from 'rxjs/operators';
import { Breakpoints, BreakpointObserver } from '@angular/cdk/layout';
import {ChartDataSets} from "chart.js";
import {ChartsModule, Color, Label} from "ng2-charts";
import {Chart} from "chart.js";
import {AdminService} from "../../../service/admin.service";
import {UserRegistrationsCounterModel} from "../../../models/UserRegistrationsCounterModel";

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  /** Based on the screen size, switch from standard to one column per row */
  cards = this.breakpointObserver.observe(Breakpoints.Handset).pipe(
    map(({matches}) => {
      if (matches) {
        return [
          {title: 'Card 1', cols: 2, rows: 1},
          {title: 'Card 2', cols: 2, rows: 1},
          {title: 'Card 3', cols: 2, rows: 1},
          {title: 'Card 4', cols: 2, rows: 1}
        ];
      }

      return [
        {title: 'Card 1', cols: 2, rows: 1},
        {title: 'Card 2', cols: 1, rows: 1},
        {title: 'Card 3', cols: 1, rows: 2},
        {title: 'Card 4', cols: 1, rows: 1}
      ];
    })
  );
  chart: Chart;

  isLoaded: boolean = false;

  constructor(private adminService: AdminService, private breakpointObserver: BreakpointObserver) {
  }

  ngOnInit(): void {
    let chartData: UserRegistrationsCounterModel;

    this.adminService.getUserRegistrationsCount().subscribe(
      (data: UserRegistrationsCounterModel) => {
        chartData = data;

      },error => {},
      () => {
        this.chart = new Chart('lineChart', {
          type: 'line',
          data: {
            labels: ["jh","8jh"],
            datasets: [
              {
                data: [1,5]
              }
            ]
          },
          options:{
            legend:{
              display: false
            },
            scales: {
              xAxes: [{
                display:true
              }],
              yAxes: [{
                display:true
              }]
            }
          }
        })
      }
    )


  }
}
