import {Component, OnInit, ViewChild} from '@angular/core';
import {Chart} from "chart.js";
import {AdminService} from "../../../../service/admin.service";
import {UserRegistrationStartEndDateModel} from "../../../../models/user/UserRegistrationStartEndDateModel";
import {Subscription} from "rxjs";
import {IncomeByDateModel} from "../../../../models/order/IncomeByDateModel";

@Component({
  selector: 'app-income-per-order-graph',
  templateUrl: './income-per-order-graph.component.html',
  styleUrls: ['./income-per-order-graph.component.css']
})
export class IncomePerOrderGraphComponent implements OnInit {
  dates: UserRegistrationStartEndDateModel;
  private subscription: Subscription;
  @ViewChild('chart') chartRef;
  private chartData: IncomeByDateModel;
  private chart: Chart;

  constructor(private adminService: AdminService) {
    this.dates = new class implements UserRegistrationStartEndDateModel {
      end: Date = new Date(Date.now());
      start: Date = new Date(Date.now() - (3600 * 1000 * 24 * 30));
    }
  }

  ngOnInit(): void {
    this.subscription = this.adminService.getIncomePerOrder(this.dates).subscribe(
      (data: IncomeByDateModel) => this.chartData = data,
        error => {
        console.log(error)
      },

      () => {
        console.log(this.chartData.date)
        let dates = []
        let income = [];

        console.log(dates)
        console.log(income)
        this.chart = new Chart(this.chartRef.nativeElement, {
          type: 'line',
          data: {
            labels: dates,
            datasets: [
              {
                borderWidth: 1,
                data: income
              }
            ]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            legend: {
              display: false
            },
            scales: {
              xAxes: [{
                display: true,
                ticks: {
                  autoSkip: true,
                  maxTicksLimit: 7
                }
              }],
              scaleLabel: {
                labelString: 'dátum',
                display: true,
              },
              yAxes: [{
                display: true,
                ticks: {
                  beginAtZero: true,
                  maxTicksLimit: 10
                },
                scaleLabel: {
                  labelString: 'vásárlók száma',
                  display: true,
                },
              }],
            }
          }
        });
      }
    )
  }
}
