import {Component, OnInit, ViewChild} from '@angular/core';
import {UserRegistrationsCounterModel} from "../../../../models/user/UserRegistrationsCounterModel";
import {Chart} from "chart.js";
import {AdminService} from "../../../../service/admin.service";
import {UserRegistrationStartEndDateModel} from "../../../../models/user/UserRegistrationStartEndDateModel";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-income-per-order-graph',
  templateUrl: './income-per-order-graph.component.html',
  styleUrls: ['./income-per-order-graph.component.css']
})
export class IncomePerOrderGraphComponent implements OnInit {
  dates: UserRegistrationStartEndDateModel;
  private subscription: Subscription;
  @ViewChild('chart') chartRef;

  constructor(private adminService: AdminService) {
    this.dates = new class implements UserRegistrationStartEndDateModel {
      end: Date = new Date(Date.now());
      start: Date = new Date(Date.now() - (3600 * 1000 * 24 * 30));
    }
  }

  ngOnInit(): void {
    this.subscription = this.adminService.getIncomePerOrder(this.dates).subscribe(
      (data: UserRegistrationsCounterModel) => {
        console.log(data)
        this.makeDataGraphCompatible(data);
      }, error => {
        console.log(error)
      },
      () => {
        // makeDataGraphCompatible()
        // this.chart = new Chart(this.chartRef.nativeElement, {
        //   type: 'bar',
        //   data: {
        //     labels: this.chartData.dates,
        //     datasets: [
        //       {
        //         borderWidth: 1,
        //         data: this.chartData.size,
        //         backgroundColor: generateRandomColor(this.chartData.size),
        //         borderColor: this.barBorderColors(),
        //       }
        //     ]
        //   },
        //   options: {
        //     responsive: true,
        //     maintainAspectRatio: false,
        //     legend: {
        //       display: false
        //     },
        //     scales: {
        //       xAxes: [{
        //         display: true,
        //         ticks: {
        //           autoSkip: true,
        //           maxTicksLimit: 7
        //         }
        //       }],
        //       scaleLabel: {
        //         labelString: 'dátum',
        //         display: true,
        //       },
        //       yAxes: [{
        //         display: true,
        //         ticks: {
        //           beginAtZero: true,
        //           maxTicksLimit: 10
        //         },
        //         scaleLabel: {
        //           labelString: 'vásárlók száma',
        //           display: true,
        //         },
        //       }],
        //     }
        //   }
        // });
      }
    )
  }

  makeDataGraphCompatible(data) {
  }



}
