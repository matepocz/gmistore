import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {Chart} from "chart.js";
import {IncomeByDateModel} from "../../../../models/order/IncomeByDateModel";

@Component({
  selector: 'app-income-per-order-graph',
  templateUrl: './income-per-order-graph.component.html',
  styleUrls: ['./income-per-order-graph.component.css']
})
export class IncomePerOrderGraphComponent implements OnInit {
  @ViewChild('incomePerDays',{static: true}) chartRef;
  @Input() chartData: IncomeByDateModel;

  chart: Chart;

  ngOnInit(): void {
    this.chart = new Chart(this.chartRef.nativeElement, {
      type: 'line',
      data: {
        labels: this.chartData.date,
        datasets: [
          {
            borderWidth: 1,
            data: this.chartData.income
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

  onChangeGraph(data:IncomeByDateModel) {
    this.chart.data.labels = data.date;
    this.chart.data.datasets[0].data = data.income;
    this.chart.update();
  }
}
