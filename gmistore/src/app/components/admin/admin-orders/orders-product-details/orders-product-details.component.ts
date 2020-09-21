import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Title} from "@angular/platform-browser";
import {AdminService} from "../../../../service/admin.service";
import {OrderService} from "../../../../service/order.service";
import {OrderDetails} from "../../../../models/order/orderDetails";
import {ActivatedRoute} from "@angular/router";
import {SubSink} from "subsink";
import {MatTableDataSource} from "@angular/material/table";

@Component({
  selector: 'app-orders-product-details',
  templateUrl: './orders-product-details.component.html',
  styleUrls: ['./orders-product-details.component.css']
})
export class OrdersProductDetailsComponent implements OnInit, OnDestroy {
  displayedColumns = ['name', 'email', 'phone'];
  displayedColumnsShipping = ['method', 'days', 'cost'];
  displayedColumnsPayment = ['paymentMethod', 'totalPrice'];
  displayedColumnStatus = ['status', 'orderedAt'];
  statusOptions: Array<string>;
  statusOption: string;
  statusValue: string = "Megs";
  subs: SubSink = new SubSink();
  orderDetailsData: OrderDetails;
  dataSource: MatTableDataSource<OrderDetails>;
  loaded: boolean = false;
  orderShippingAddressForm: FormGroup;
  orderBillingAddressForm: FormGroup;
  statusForm: FormGroup;

  constructor(private titleService: Title,
              private adminService: AdminService,
              private orderService: OrderService,
              private activatedRoute: ActivatedRoute,
              private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.titleService.setTitle("Megrendelés - GMI Store");

    this.orderShippingAddressForm = this.fb.group({
      shippingAddress: this.fb.group({
        city: [''],
        street: [''],
        number: ['', Validators.pattern("^[0-9]*$")],
        floor: [''],
        door: ['', Validators.pattern("^[0-9]*$")],
        country: [''],
        postcode: ['', Validators.pattern("^[0-9]*$")]
      }),
    });

    this.orderBillingAddressForm = this.fb.group({
      billingAddress: this.fb.group({
        city: [''],
        street: [''],
        number: ['', Validators.pattern("^[0-9]*$")],
        floor: [''],
        door: ['', Validators.pattern("^[0-9]*$")],
        country: [''],
        postcode: ['', Validators.pattern("^[0-9]*$")]
      }),
    });

    this.subs.add(this.orderService.getStatusOptions().subscribe(
      (data: Array<string>) => {
        this.statusOptions = data;
      }, (error) => console.log(error)
    ));

    this.subs.add(this.activatedRoute.paramMap.subscribe(
      paramMap => {
        const editableId: string = paramMap.get('id');
        if (editableId) {
          this.fetchOrderDetailsData(editableId);
        }
      },
      error => console.warn(error),
    ));
  }

  fetchOrderDetailsData(id: string) {
    this.subs.add(
      this.orderService.fetchOrderDetails(id).subscribe(
        (data) => {
          this.orderDetailsData = data;
        }, (error) => console.log(error),
        () => {
          let dataSource = [this.orderDetailsData];
          this.dataSource = new MatTableDataSource<OrderDetails>(dataSource);
          this.loaded = true;
          console.log(dataSource)
        }
      ));
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }
}
