import { Component, OnInit } from '@angular/core';
import {FormGroup} from "@angular/forms";
import {Title} from "@angular/platform-browser";
import {AdminService} from "../../../../service/admin.service";
import {OrderService} from "../../../../service/order.service";

@Component({
  selector: 'app-orders-product-details',
  templateUrl: './orders-product-details.component.html',
  styleUrls: ['./orders-product-details.component.css']
})
export class OrdersProductDetailsComponent implements OnInit {
  dataSource: any;
  displayedColumns = ['name','email','phone'];
  form: FormGroup;
  statusOptions: Array<string>;
  statusOption: string;
  statusValue: string = "Megs";

  constructor(private titleService: Title,
              private adminService: AdminService,
              private orderService: OrderService) {
  }

  ngOnInit(): void {
    this.titleService.setTitle("Megrendelés - GMI Store")
    this.fetchOrderDetailsData();
    this.orderService.getStatusOptions().subscribe(
      (data: Array<string>) => {
        this.statusOptions = data;
      }, (error) => console.log(error)
    )
  }

  private fetchOrderDetailsData() {

  }
}
