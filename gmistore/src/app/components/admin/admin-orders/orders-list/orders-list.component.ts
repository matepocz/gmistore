import {Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {MatTableDataSource} from "@angular/material/table";
import {AdminService} from "../../../../service/admin.service";
import {AuthService} from "../../../../service/auth-service";
import {Router} from "@angular/router";
import {OrderListModel} from "../../../../models/order/orderListModel";
import {Subscription} from "rxjs";
import {MediaChange, MediaObserver} from "@angular/flex-layout";

@Component({
  selector: 'app-orders-list',
  templateUrl: './orders-list.component.html',
  styleUrls: ['./orders-list.component.css']
})
export class OrdersListComponent implements OnInit {
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  displayedColumns: string[] = ['id', 'username', 'date', 'status', 'totalPrice'];
  orderData: Array<OrderListModel>;
  dataSource: MatTableDataSource<OrderListModel>;
  subscription: Subscription;
  currentScreenWidth: string = '';
  flexMediaWatcher: Subscription;

  constructor(private adminService: AdminService,
              private authService: AuthService,
              private router: Router,
              private mediaObserver:MediaObserver) {
  }

  ngOnInit(): void {
    this.getOrders();
  }

  getOrders() {
    this.subscription = (this.adminService.getOrders().subscribe(
      (data) => this.orderData = data,
      error => console.log(error),
      () => {
        this.dataSource = new MatTableDataSource(this.orderData);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      }
    ));

    this.flexMediaWatcher = this.mediaObserver.media$.subscribe((change: MediaChange) => {
      if (change.mqAlias !== this.currentScreenWidth) {
        this.currentScreenWidth = change.mqAlias;
        this.setupTable();
      }
    });

  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  editOrder(id: string) {
    this.router.navigate(['admin/orders/edit', id])
  }

  setupTable = () => {
    if (this.currentScreenWidth === 'xs' || this.currentScreenWidth === 's') { // only display internalId on larger screens
      let displayedColumns = this.displayedColumns;
      this.displayedColumns = displayedColumns.filter(str => !str.match(/^(date|status|totalPrice)$/)); // remove 'internalId'
      console.log(this.displayedColumns)
    } else if (!this.displayedColumns.includes("date" || "totalPrice" || "status")) {
      this.displayedColumns.push("date", "status","totalPrice");
    }
  };

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

}
