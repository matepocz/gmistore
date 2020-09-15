import {Component, Input, OnInit} from '@angular/core';
import { FormGroup} from "@angular/forms";


@Component({
  selector: 'app-shipping-address',
  templateUrl: './shipping-address.component.html',
  styleUrls: ['./shipping-address.component.css']
})
export class ShippingAddressComponent implements OnInit {

  @Input() userDataForm: FormGroup;
  @Input() userShipping: any;

  constructor() {
  }

  ngOnInit() {
    this.userDataForm.patchValue({
      shippingAddress: {
        city: this.userShipping?.city,
        street: this.userShipping?.street,
        number: this.userShipping?.number,
        floor: this.userShipping?.floor,
        door: this.userShipping?.door,
        country: this.userShipping?.country,
        postcode: this.userShipping?.postcode
      },
    })
  }
}

