import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CartService} from "../../service/cart-service";
import {AuthService} from "../../service/auth-service";
import {OrderService} from "../../service/order.service";
import {Subscription} from "rxjs";
import {CustomerDetailsModel} from "../../models/customer-details.model";
import {CartModel} from "../../models/cart-model";

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit, OnDestroy {

  isLinear: true;
  loading: boolean = true;
  authenticatedUser: boolean;

  cartDetails: CartModel;
  customerDetails: CustomerDetailsModel;

  detailsForm: FormGroup;
  shippingAddressForm: FormGroup;
  billingAddressForm: FormGroup;

  customerDetailsSub: Subscription;
  cartSub: Subscription;

  constructor(private formBuilder: FormBuilder, private cartService: CartService,
              private authService: AuthService, private orderService: OrderService) {
  }

  ngOnInit(): void {
    this.authenticatedUser = this.authService.isAuthenticated();

    this.detailsForm = this.formBuilder.group({
      firstName: [null],
      lastName: [null],
      email: [null, Validators.compose([Validators.required, Validators.email])],
      phoneNumber: [null]
    });

    this.shippingAddressForm = this.formBuilder.group({
      id: [null],
      city: [null, Validators.required],
      street: [null],
      number: [null],
      floor: [null],
      door: [null],
      country: [null],
      postcode: [null]
    });

    this.billingAddressForm = this.formBuilder.group({
      id: [null],
      city: [null],
      street: [null],
      number: [null],
      floor: [null],
      door: [null],
      country: [null],
      postcode: [null]
    });

    if (this.authenticatedUser) {
      this.customerDetailsSub = this.orderService.getCustomerDetails().subscribe(
        (response) => {
          console.log(response);
          this.customerDetails = response;
        }, (error) => {
          console.log(error);
        },
        () => {
          this.detailsForm.patchValue({
            firstName: this.customerDetails.firstName,
            lastName: this.customerDetails.lastName,
            email: this.customerDetails.email,
            phoneNumber: this.customerDetails.phoneNumber
          });

          this.shippingAddressForm.patchValue({
            id: this.customerDetails.shippingAddress.id,
            city: this.customerDetails.shippingAddress.city,
            street: this.customerDetails.shippingAddress.street,
            number: this.customerDetails.shippingAddress.number,
            floor: this.customerDetails.shippingAddress.floor,
            door: this.customerDetails.shippingAddress.door,
            country: this.customerDetails.shippingAddress.country,
            postcode: this.customerDetails.shippingAddress.postcode
          });

          this.billingAddressForm.patchValue({
            id: this.customerDetails.billingAddress.id,
            city: this.customerDetails.billingAddress.city,
            street: this.customerDetails.billingAddress.street,
            number: this.customerDetails.billingAddress.number,
            floor: this.customerDetails.billingAddress.floor,
            door: this.customerDetails.billingAddress.door,
            country: this.customerDetails.billingAddress.country,
            postcode: this.customerDetails.billingAddress.postcode
          });
        }
      )
    }

    this.cartSub = this.cartService.getCart().subscribe(
      (response) => {
        console.log(response);
        this.cartDetails = response;
      }, (error) => {
        console.log(error);
        this.loading = false;
      },
      () => {
        this.loading = false;
      }
    );

  }

  copyShippingDetails() {

  }

  onSubmit() {
    console.log(this.customerDetails);
  }

  ngOnDestroy() {
    this.cartSub.unsubscribe();
    if (this.customerDetailsSub) {
      this.customerDetailsSub.unsubscribe();
    }
  }
}
