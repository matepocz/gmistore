import {Component, Input, OnInit} from '@angular/core';
import {UserModel} from "../../../../../models/user-model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SharingService} from "../../../../../service/sharing.service";

@Component({
  selector: 'app-shipping-address',
  templateUrl: './shipping-address.component.html',
  styleUrls: ['./shipping-address.component.css']
})
export class ShippingAddressComponent implements OnInit {

  @Input() userDataForm: FormGroup
  userData: UserModel

  constructor(private fb: FormBuilder, private sharedService: SharingService) {

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
    this.sharedService.sharedMessage.subscribe(message => {
      this.userData = message;
    })

    this.userDataForm.patchValue({
      shippingAddress: {
        city: this.userData.shippingAddress.city,
        street: this.userData.shippingAddress.street,
        number: this.userData.shippingAddress.number,
        floor: this.userData.shippingAddress.floor,
        door: this.userData.shippingAddress.door,
        country: this.userData.shippingAddress.country,
        postcode: this.userData.shippingAddress.postcode
      },
    })
  }
}

