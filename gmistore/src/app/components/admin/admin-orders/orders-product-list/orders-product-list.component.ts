import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-orders-product-list',
  templateUrl: './orders-product-list.component.html',
  styleUrls: ['./orders-product-list.component.css']
})
export class OrdersProductListComponent implements OnInit {
  dataSource: any;
  displayedColumns = ['name','email','phone'];

  constructor() { }

  ngOnInit(): void {
  }

}
