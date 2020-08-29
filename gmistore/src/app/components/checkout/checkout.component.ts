import {Component, OnInit} from '@angular/core';
import {AddressModel} from "../../models/address-model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CartService} from "../../service/cart-service";
import {AuthService} from "../../service/auth-service";

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {

  isLinear: true;
  loading: boolean = true;
  authenticatedUser: boolean;

  shippingAddress: AddressModel;
  billingAddress: AddressModel;

  shippingAddressForm: FormGroup;
  billingAddressForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private cartService: CartService,
              private authService: AuthService) {
  }

  ngOnInit(): void {
    this.authenticatedUser = this.authService.isAuthenticated();
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
    if (this.authenticatedUser){

    }

    this.loading = false;
  }

}
