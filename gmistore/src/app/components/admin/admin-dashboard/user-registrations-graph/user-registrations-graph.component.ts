import {ChangeDetectorRef, Component, Input, OnInit, ViewChild} from '@angular/core';
import {UserRegistrationsCounterModel} from "../../../../models/user/UserRegistrationsCounterModel";
import {Chart} from "chart.js";
import {generateRandomColor} from "../../../../utils/generate-color";
import {Subscription} from "rxjs";
import {UserRegistrationStartEndDateModel} from "../../../../models/user/UserRegistrationStartEndDateModel";
import {AdminService} from "../../../../service/admin.service";
import {PopupSnackbar} from "../../../../utils/popup-snackbar";
import {LiveDataSubjectService} from "../../../../service/live-data-subject.service";

@Component({
  selector: 'app-user-registrations-graph',
  templateUrl: './user-registrations-graph.component.html',
  styleUrls: ['./user-registrations-graph.component.css']
})
export class UserRegistrationsGraphComponent implements OnInit {
  @ViewChild('lineChart', {static: true}) private chartRef;
  @Input() chart: Chart;
  @Input() chartData: UserRegistrationsCounterModel;
  subscription: Subscription;

  constructor(private adminService: AdminService,
              private snackBar: PopupSnackbar,
              private subj: LiveDataSubjectService,
              private cdRef: ChangeDetectorRef) {
  }

  ngOnInit(): void {
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

  barBorderColors = () => {
    let color = '#851a2a';
    let colors = [];
    for (let j = 0; j < this.chartData.size.length; j++) {
      colors.push(color)
    }
    return colors;
  }

  onChangeGraph(chartData:UserRegistrationsCounterModel) {
    console.log("Native works")
    this.chart.data.labels = chartData.dates;
    this.chart.data.datasets[0].data = chartData.size;
    this.chart.update();
  }

}
