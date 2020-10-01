import {ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Chart} from "chart.js";
import {AdminService} from "../../../service/admin.service";
import {UserRegistrationsCounterModel} from "../../../models/user/UserRegistrationsCounterModel";
import {UserRegistrationStartEndDateModel} from "../../../models/user/UserRegistrationStartEndDateModel";
import {Subscription} from "rxjs";
import {MatCalendar} from "@angular/material/datepicker";
import {PopupSnackbar} from "../../../utils/popup-snackbar";
import {LiveDataSubjectService} from "../../../service/live-data-subject.service";
import {ProductService} from "../../../service/product-service";
import {IncomeSpentModel} from "../../../models/incomeSpentModel";
import {IncomeByDateModel} from "../../../models/order/IncomeByDateModel";
import {DashBoardBasicModel} from "../../../models/DashBoardBasicModel";
import {SatDatepickerInputEvent} from "saturn-datepicker";
import {UserRegistrationsGraphComponent} from "./user-registrations-graph/user-registrations-graph.component";
import {IncomePerOrderGraphComponent} from "./income-per-order-graph/income-per-order-graph.component";


@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit, OnDestroy {

  // @ViewChild('lineChart') private chartRef;
  @ViewChild(UserRegistrationsGraphComponent) registeredUsers: UserRegistrationsGraphComponent;
  @ViewChild(IncomePerOrderGraphComponent) incomeByDays: IncomePerOrderGraphComponent;

  chart: Chart;
  date: any;
  liveDataSize: any;
  chartData: UserRegistrationsCounterModel;
  minDate: Date;
  maxDate: Date;
  dateInterval: UserRegistrationStartEndDateModel;
  items: any[] = [''];
  pieChartData: IncomeSpentModel;
  incomeLineChartData: IncomeByDateModel;
  dates: { end: Date; start: Date };
  dashboardData: DashBoardBasicModel;
  liveItem: any;
  subs: Subscription[] = [];


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
    this.getExchangeData();

    this.subs.push(this.adminService.getIncomePerOrder(this.dates).subscribe(
      (data: IncomeByDateModel) => this.incomeLineChartData = data,
      error => console.log(error)
    ));

    this.subs.push(this.adminService.getIncomePerOrder(this.dates).subscribe(
      (data: IncomeByDateModel) => this.incomeLineChartData = data,
      error => console.log(error)
    ));

    this.subs.push(this.adminService.fetchDashboardData().subscribe(
      (data: DashBoardBasicModel) => this.dashboardData = data,
      error => console.log(error)
    ));

    this.subs.push(this.productService.getIncomeAndSpent().subscribe(
      (data) => this.pieChartData = data,
      (err) => console.log(err),
      () => console.log(this.chartData)
    ));

    this.subs.push(this.subj.asObservable().subscribe(data => {
      this.liveItem = data;
      this.liveDataSize = Object.keys(this.liveItem).length;
      this.cdRef.detectChanges();
    }));

    this.subs.push(this.adminService.getUserRegistrationsCount().subscribe(
      (data: UserRegistrationsCounterModel) => {
        this.chartData = data;
      }, error => console.log(error)
    ))
  }

  getOrdersByDays(dates) {
    this.subs.push(this.adminService.getIncomePerOrder(dates).subscribe(
      (data: IncomeByDateModel) => this.incomeLineChartData = data,
      error => console.log(error),
      () => this.incomeByDays.onChangeGraph(this.incomeLineChartData)
    ));
  }

  ngOnDestroy() {
    this.subs.map(sub => sub.unsubscribe());
  }

  inlineRangeChange($event: any) {
    //30 days
    if ($event.value.begin.getTime() + (1000 * 60 * 60 * 24 * 30) > $event.value.end.getTime()) {
      this.dateInterval.start = $event.value.begin;
      this.dateInterval.end = $event.value.end;

      this.subs.push(this.adminService.getUserRegistrationsCountByDate(this.dateInterval).subscribe(
        (data) => this.chartData = data,
        (err) => console.log(err),
        () => {
          this.registeredUsers.onChangeGraph(this.chartData)
        }
      ));

      this.getOrdersByDays(this.dateInterval);

      this.incomeByDays.onChangeGraph(this.incomeLineChartData);
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

  getExchangeData() {
    this.adminService.getExchangeData();
  }

  stopExchangeUpdates() {
    this.adminService.stopExchangeUpdates();
  }

  onDateInput($event: SatDatepickerInputEvent<Date>) {
    this.snackBar.popUp('Use the calendar instead ' + $event)
  }

  getIconForUser(role: string) {
    if (role === 'ROLE_USER') {
      return 'person';
    }
    if (role === 'ROLE_SELLER') {
      return 'shopping_bag';
    }
    if (role === 'ROLE_ADMIN') {
      return 'admin_panel_settings';
    }
  }
}
