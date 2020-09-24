import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Chart} from "chart.js";
import {AdminService} from "../../../service/admin.service";
import {UserRegistrationsCounterModel} from "../../../models/user/UserRegistrationsCounterModel";
import {UserRegistrationStartEndDateModel} from "../../../models/user/UserRegistrationStartEndDateModel";
import {Subscription} from "rxjs";
import {MatCalendar} from "@angular/material/datepicker";

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit, OnDestroy {
  @ViewChild('lineChart') private chartRef;
  chart: Chart;
  chartData: UserRegistrationsCounterModel;
  subscription: Subscription;
  selectedDate: any;
  minDate: Date;
  maxDate: Date;
  dateInterval: UserRegistrationStartEndDateModel = new class implements UserRegistrationStartEndDateModel {
    end: Date;
    start: Date;
  };

  constructor(private adminService: AdminService) {
  }

  @ViewChild(MatCalendar) _datePicker: MatCalendar<Date>


  ngOnInit(): void {
    this.subscription = this.adminService.getUserRegistrationsCount().subscribe(
      (data: UserRegistrationsCounterModel) => {
        this.chartData = data;

      }, error => {
        console.log(error)
      },
      () => {
        this.chart = new Chart(this.chartRef.nativeElement, {
          type: 'bar',
          data: {
            labels: this.chartData.dates,
            datasets: [
              {
                borderWidth: 1,
                data: this.chartData.size,
                backgroundColor: this.barColors(),
                borderColor: this.barBorderColors(),
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

  barColors = () => {
    let letters = '0123456789ABCDEF'.split('');
    let color = '#E6'; //transparency 90%
    let colors = [];
    for (let j = 0; j < this.chartData.size.length; j++) {
      for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
      }
      colors.push(color)
      color = '#E6';
    }
    return colors;
  }

  barBorderColors = () => {
    let color = '#851a2a';
    let colors = [];
    for (let j = 0; j < this.chartData.size.length; j++) {
      colors.push(color)
    }
    return colors;
  }


  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  inlineRangeChange($event: any) { //30 days
    if ($event.begin.getTime() + (1000 * 60 * 60 * 24 * 30) > $event.end.getTime()) {
      this.dateInterval.start = $event.begin;
      this.dateInterval.end = $event.end;

      this.adminService.getUserRegistrationsCountByDate(this.dateInterval).subscribe(
        (data) => this.chartData = data,
        (err) => console.log(err),
        () => {
          console.log(this.chartData)
          this.chart = new Chart(this.chartRef.nativeElement, {
            type: 'bar',
            data: {
              labels: this.chartData.dates,
              datasets: [
                {
                  borderWidth: 1,
                  data: this.chartData.size,
                  backgroundColor: this.barColors(),
                  borderColor: this.barBorderColors(),
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
      console.log("in range")
      this.chartData.size = [1,2]
      this.chartData.dates = ["2","3"]
    } else {
      console.log("out of range")
    }
  }
}
