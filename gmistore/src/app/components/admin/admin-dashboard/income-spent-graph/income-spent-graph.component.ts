import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {IncomeSpentModel} from "../../../../models/incomeSpentModel";
import * as Chart from "chart.js";

@Component({
  selector: 'app-income-spent-graph',
  templateUrl: './income-spent-graph.component.html',
  styleUrls: ['./income-spent-graph.component.css']
})
export class IncomeSpentGraphComponent implements OnInit {
  @ViewChild('myPieChart') chartRef;
  @Input() chartData: IncomeSpentModel;
  myPieChart: Chart;

  constructor() {
  }

  ngOnInit(): void {
    console.log(this.chartData)
    this.myPieChart = new Chart(this.chartRef.nativeElement, {
      type: 'pie',
      data: {
        datasets: [{
          data: [this.chartData.income, this.chartData.spent, this.chartData.discount],
          backgroundColor: ['red', 'yellow', 'green']
        }],
      },
      options: {
        responsive: true,
        legend: {
          position: "right",
          labels: {
            usePointStyle: true
          }
        }
      }
    });
  }
}
