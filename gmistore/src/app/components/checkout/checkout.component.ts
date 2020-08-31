import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CartService} from "../../service/cart-service";
import {AuthService} from "../../service/auth-service";
import {OrderService} from "../../service/order.service";
import {Subscription} from "rxjs";
import {CustomerDetailsModel} from "../../models/customer-details.model";
import {CartModel} from "../../models/cart-model";
import {Title} from "@angular/platform-browser";
import {SideNavComponent} from "../side-nav/side-nav.component";

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
  createOrderSub: Subscription;

  constructor(private formBuilder: FormBuilder, private cartService: CartService,
              private authService: AuthService, private orderService: OrderService,
              private titleService: Title, private sideNav: SideNavComponent) {
  }

  ngOnInit(): void {
    this.titleService.setTitle("Kosár véglegesítése - GMI Store")
    this.authenticatedUser = this.authService.isAuthenticated();

    this.detailsForm = this.formBuilder.group({
      firstName: [null],
      lastName: [null],
      email: [null, Validators.compose([Validators.required, Validators.email])],
      phoneNumber: [null]
    });

    this.shippingAddressForm = this.formBuilder.group({
      id: [null],
      city: [null,
        Validators.compose([Validators.required, Validators.minLength(3)])],
      street: [null,
        Validators.compose([Validators.required, Validators.minLength(3)])],
      number: [null,
        Validators.compose([Validators.required, Validators.min(1)])],
      floor: [null],
      door: [null],
      country: [null,
        Validators.compose([Validators.required, Validators.minLength(3)])],
      postcode: [null,
        Validators.compose([Validators.required, Validators.minLength(3)])]
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
    this.billingAddressForm.patchValue({
      id: this.shippingAddressForm.get('id').value,
      city: this.shippingAddressForm.get('city').value,
      street: this.shippingAddressForm.get('street').value,
      number: this.shippingAddressForm.get('number').value,
      floor: this.shippingAddressForm.get('floor').value,
      door: this.shippingAddressForm.get('door').value,
      country: this.shippingAddressForm.get('country').value,
      postcode: this.shippingAddressForm.get('postcode').value,
    });
  }

  onSubmit() {
    this.loading = true;
    this.customerDetails.firstName = this.detailsForm.get('firstName').value;
    this.customerDetails.lastName = this.detailsForm.get('lastName').value;
    this.customerDetails.email = this.detailsForm.get('email').value;
    this.customerDetails.phoneNumber = this.detailsForm.get('phoneNumber').value;
    this.customerDetails.shippingAddress = this.shippingAddressForm.value;
    this.customerDetails.billingAddress = this.billingAddressForm.value;

    console.log(this.customerDetails);
    this.createOrderSub = this.orderService.createOrder(this.customerDetails).subscribe(
      (response) => {
        console.log(response);
      }, (error) => {
        console.log(error);
        this.loading = false;
      },
      () => {
        this.sideNav.updateItemsInCart(0);
        this.loading = false;
      }
    )
  }

  ngOnDestroy() {
    this.cartSub.unsubscribe();
    if (this.customerDetailsSub) {
      this.customerDetailsSub.unsubscribe();
    }
    if (this.createOrderSub) {
      this.createOrderSub.unsubscribe();
    }
  }
}
