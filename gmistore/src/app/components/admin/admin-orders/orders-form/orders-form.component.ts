import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {OrderDetails} from "../../../../models/order/orderDetails";
import {SubSink} from "subsink";
import {OrderService} from "../../../../service/order.service";

@Component({
  selector: 'app-orders-form',
  templateUrl: './orders-form.component.html',
  styleUrls: ['./orders-form.component.css']
})
export class OrdersFormComponent implements OnInit {
  id: string;
  private subs = new SubSink();
  orderDetailsData: OrderDetails;

  constructor(private activatedRoute: ActivatedRoute,
              private orderService: OrderService) { }

  ngOnInit(): void {
    (this.activatedRoute.paramMap.subscribe(
      paramMap => {
        const editableId: string = paramMap.get('id');
        if (editableId) {
          this.id = editableId;
        }
      },
      error => console.warn(error),
    ));

    this.fetchOrderDetailsData(this.id);
  }

  fetchOrderDetailsData(id: string) {
    this.subs.add(this.orderService.fetchOrderDetails(id).subscribe(
      (data) => {
        this.orderDetailsData = data;
      }, (error) => console.log(error),
    ));
  }
}
