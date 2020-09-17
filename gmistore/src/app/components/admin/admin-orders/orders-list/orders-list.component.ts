import {Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {UserListDetailsModel} from "../../../../models/UserListDetailsModel";
import {MatTableDataSource} from "@angular/material/table";
import {SubSink} from "subsink";
import {AdminService} from "../../../../service/admin.service";
import {AuthService} from "../../../../service/auth-service";
import {Router} from "@angular/router";
import {MatSlideToggleChange} from "@angular/material/slide-toggle";
import {UserIsActiveModel} from "../../../../models/userIsActiveModel";
import {OrderListModel} from "../../../../models/order/orderListModel";
import {Subscription} from "rxjs";

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

  constructor(private adminService: AdminService,
              private authService: AuthService,
              private router: Router) {
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

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

}
