import { Component, OnInit } from '@angular/core';
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-orders-product-details',
  templateUrl: './orders-product-details.component.html',
  styleUrls: ['./orders-product-details.component.css']
})
export class OrdersProductDetailsComponent implements OnInit {
  dataSource: any;
  displayedColumns = ['name','email','phone'];
  openB = false;
  openS = false;
  form: FormGroup;
  foods: any;
  foodControl: any;
  carControl: any;
  cars: any;

  constructor() { }

  ngOnInit(): void {
  }

  dropdown(choice:string) {
    if (choice === 'shipping') {
      this.openS = !this.openS;
    } else {
      this.openB = !this.openB;
    }
  }
}
