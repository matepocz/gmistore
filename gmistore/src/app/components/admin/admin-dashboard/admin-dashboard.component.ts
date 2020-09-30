import {ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Chart} from "chart.js";
import {AdminService} from "../../../service/admin.service";
import {UserRegistrationsCounterModel} from "../../../models/user/UserRegistrationsCounterModel";
import {UserRegistrationStartEndDateModel} from "../../../models/user/UserRegistrationStartEndDateModel";
import {Subscription} from "rxjs";
import {MatCalendar} from "@angular/material/datepicker";
import {generateRandomColor} from "../../../utils/generate-color";
import {PopupSnackbar} from "../../../utils/popup-snackbar";
import {LiveDataSubjectService} from "../../../service/live-data-subject.service";
import {ProductService} from "../../../service/product-service";
import {IncomeSpentModel} from "../../../models/incomeSpentModel";
import {IncomeByDateModel} from "../../../models/order/IncomeByDateModel";
import {DashBoardBasicModel} from "../../../models/DashBoardBasicModel";


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
  dateInterval: UserRegistrationStartEndDateModel;
  items: any[] = [''];
  pieChartData: IncomeSpentModel;
  incomeLineChartData: IncomeByDateModel;
  private dates: { end: Date; start: Date };
  dashboardData: DashBoardBasicModel;

  constructor(private adminService: AdminService,
              private productService: ProductService,
              private snackBar: PopupSnackbar,
              private subj: LiveDataSubjectService,
              private cdRef: ChangeDetectorRef) {

    this.dates = new class implements UserRegistrationStartEndDateModel {
      end: Date = new Date(Date.now());
      start: Date = new Date(Date.now() - (3600 * 1000 * 24 * 30));

    }

    this.dateInterval = new class implements UserRegistrationStartEndDateModel {
      end: Date;
      start: Date;
    };
  }

  @ViewChild(MatCalendar) _datePicker: MatCalendar<Date>


  ngOnInit(): void {
    this.subscription = this.adminService.getIncomePerOrder(this.dates).subscribe(
      (data: IncomeByDateModel) => this.incomeLineChartData = data,
      error => console.log(error)
    );

    this.subscription = this.adminService.fetchDashboardData().subscribe(
      (data: DashBoardBasicModel) => this.dashboardData = data,
      error => console.log(error)
    );

    this.productService.getIncomeAndSpent().subscribe(
      (data) => this.pieChartData = data,
      (err) => console.log(err),
      () =>
        console.log(this.chartData))

    this.subj.asObservable().subscribe(data => {
      this.items.push(data);
      this.cdRef.detectChanges();
      console.log(this.items);
    })

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
                backgroundColor: generateRandomColor(this.chartData.size),
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

  inlineRangeChange($event
                      :
                      any
  ) { //30 days
    if ($event.begin.getTime() + (1000 * 60 * 60 * 24 * 30) > $event.end.getTime()) {
      this.dateInterval.start = $event.begin;
      this.dateInterval.end = $event.end;

      this.adminService.getUserRegistrationsCountByDate(this.dateInterval).subscribe(
        (data) => this.chartData = data,
        (err) => console.log(err),
        () => {
          this.chart.data.labels = this.chartData.dates;
          this.chart.data.datasets[0].data = this.chartData.size;
          this.chart.update();
        }
      )

      this.adminService.getIncomePerOrder(this.dateInterval).subscribe(
        (data) => console.log(data),
        (err) => console.log(err),
      )

      console.log("in range")
      this.chartData.size = [1, 2]
      this.chartData.dates = ["2", "3"]
    } else {
      console.log("out of range")
      this.snackBar.popUp("Sikertelen szűrés, a napok száma nem haladhatja meg a 30-at!");
    }
  }

  GetExchangeData() {
    this.adminService.GetExchangeData();
  }

  stopExchangeUpdates() {
    this.adminService.stopExchangeUpdates();
  }
}
