import {Component, Input, OnInit} from '@angular/core';
import {OrderDetails} from "../../../../models/order/orderDetails";
import {MatTableDataSource} from "@angular/material/table";

@Component({
  selector: 'app-orders-product-list',
  templateUrl: './orders-product-list.component.html',
  styleUrls: ['./orders-product-list.component.css']
})
export class OrdersProductListComponent implements OnInit {
  @Input() orderDetailsInput: OrderDetails;
  dataSource: MatTableDataSource<any>;
  displayedColumns = ['pic','name','quantity','price'];

  constructor() {
  }

  ngOnInit(): void {
    let dataSource = this.orderDetailsInput.items;
    console.log(dataSource);
    this.dataSource = new MatTableDataSource<any>(dataSource);
  }

}
