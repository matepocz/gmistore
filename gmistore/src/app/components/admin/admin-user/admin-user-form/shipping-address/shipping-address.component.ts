import {Component, Input, OnInit} from '@angular/core';
import {UserModel} from "../../../../../models/user-model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SharingService} from "../../../../../service/sharing.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-shipping-address',
  templateUrl: './shipping-address.component.html',
  styleUrls: ['./shipping-address.component.css']
})
export class ShippingAddressComponent implements OnInit {

  @Input() userDataForm: FormGroup;
 // userData: UserModel;
  @Input() userShipping: any;

  constructor(private fb: FormBuilder,
              private sharedService: SharingService,
              private route: ActivatedRoute) {

    this.userDataForm = this.fb.group({
      shippingAddress: this.fb.group({
        city: [''],
        street: [''],
        number: [''],
        floor: [''],
        door: [''],
        country: [''],
        postcode: ['']
      })
    });
  }

  ngOnInit() {
    // this.sharedService.sharedMessage.subscribe(message => {
    //   this.userData = message;



    this.userDataForm.patchValue({
      shippingAddress: {
        city: this.userShipping.shippingAddress.city,
        street: this.userShipping.shippingAddress.street,
        number: this.userShipping.shippingAddress.number,
        floor: this.userShipping.shippingAddress.floor,
        door: this.userShipping.shippingAddress.door,
        country: this.userShipping.shippingAddress.country,
        postcode: this.userShipping.shippingAddress.postcode
      },
    })
  }
}

