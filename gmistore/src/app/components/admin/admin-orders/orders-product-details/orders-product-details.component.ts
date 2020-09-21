import { Component, OnInit } from '@angular/core';
import {FormGroup} from "@angular/forms";
import {Title} from "@angular/platform-browser";
import {AdminService} from "../../../../service/admin.service";

@Component({
  selector: 'app-orders-product-details',
  templateUrl: './orders-product-details.component.html',
  styleUrls: ['./orders-product-details.component.css']
})
export class OrdersProductDetailsComponent implements OnInit {
  dataSource: any;
  displayedColumns = ['name','email','phone'];
  form: FormGroup;
  paymentMethods: string[] =  ['any', 'asdasd', 'asdsa'];
  paymentMethod: string;
  private statusOptions: Array<string>;

  constructor(private titleService: Title,
              private adminService: AdminService) { }

  ngOnInit(): void {
    this.titleService.setTitle("Megrendelés - GMI Store")
    this.fetchOrderDetailsData();
    this.adminService.getStatusOptions().subscribe(
      (data: Array<string>) => {
        this.statusOptions = data;
      }, (error) => console.log(error)
    )
  }

  private fetchOrderDetailsData() {

  }
}
