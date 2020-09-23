import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Title} from "@angular/platform-browser";
import {AdminService} from "../../../../service/admin.service";
import {OrderService} from "../../../../service/order.service";
import {OrderDetails} from "../../../../models/order/orderDetails";
import {ActivatedRoute} from "@angular/router";
import {SubSink} from "subsink";
import {MatTableDataSource} from "@angular/material/table";
import {AddressModel} from "../../../../models/address-model";
import {errorHandler} from "../../../../utils/error-handler";
import {MatSelectChange} from "@angular/material/select";
import {OrderStatusOptionsModel} from "../../../../models/order/orderStatusOptionsModel";

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
  statusOptions: Array<OrderStatusOptionsModel>;
  statusOption: string;
  statusValue: string = "Megs";
  subs: SubSink = new SubSink();
  orderDetailsData: OrderDetails;
  dataSource: MatTableDataSource<OrderDetails>;
  statusDataSource: MatTableDataSource<OrderStatusOptionsModel>;
  loaded: boolean = false;
  orderShippingAddressForm: FormGroup;
  orderBillingAddressForm: FormGroup;
  statusForm: FormGroup;
  val: string[];
  private id: string;

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

    this.statusForm = this.fb.group({
      status: [''],
    });

    this.subs.add(this.orderService.getStatusOptions().subscribe(
      (data: Array<OrderStatusOptionsModel>) => {
        this.statusOptions = data;
      }, (error) => console.log(error)
    ));

    this.subs.add(this.activatedRoute.paramMap.subscribe(
      paramMap => {
        const editableId: string = paramMap.get('id');
        this.id = editableId;
        if (editableId) {
          this.fetchOrderDetailsData(editableId);
        }
      },
      error => console.warn(error),
    ));
  }

  fetchOrderDetailsData(id: string) {
    this.subs.add(this.orderService.fetchOrderDetails(id).subscribe(
      (data) => {
        this.orderDetailsData = data;
        this.val = data.status.map(s => s.value);
        console.log(this.val);
      }, (error) => console.log(error),
      () => {
        let dataSource = [this.orderDetailsData];
        this.dataSource = new MatTableDataSource<OrderDetails>(dataSource);
        this.statusDataSource = new MatTableDataSource<OrderStatusOptionsModel>(dataSource[0].status);
        this.loaded = true;
        console.log(dataSource)
      }
    ));
  }

  onSubmitAddress(id: string) {
    const data = this.orderShippingAddressForm.value;
    if (data.shippingAddress) {
      console.log(data.shippingAddress)
      this.updateDeliveryAddress(id, data.shippingAddress)
    } else if (data.billingAddress) {
      this.updateInvoiceAddress(id, data.billingAddress)
    }
  }

  private updateInvoiceAddress(id: string, data: AddressModel) {
    this.subs.add(this.orderService.updateInvoiceAddress(id, data).subscribe(
      () => console.log(data),
      error => errorHandler(error, this.orderBillingAddressForm),
    ));
  }

  private updateDeliveryAddress(id: string, data: AddressModel) {
    this.subs.add(this.orderService.updateDeliveryAddress(id, data).subscribe(
      () => console.log(data),
      error => errorHandler(error, this.orderBillingAddressForm),
    ));
  }

  changeStatus($event: MatSelectChange) {
    if($event.value) {
    this.subs.add(this.orderService.updateOrderStatus(this.id, $event.value).subscribe(
      () => console.log("message has been sent"),
      error => {console.log(error)},
      () => this.fetchOrderDetailsData(this.id)
      )
    )}
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

}
