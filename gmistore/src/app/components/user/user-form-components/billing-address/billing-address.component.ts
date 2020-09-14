import {Component, Input, OnInit} from '@angular/core';
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-billing-address',
  templateUrl: './billing-address.component.html',
  styleUrls: ['./billing-address.component.css']
})
export class BillingAddressComponent implements OnInit {
  @Input() userDataForm: FormGroup
  @Input() userBilling: any
  constructor() {

  }

  ngOnInit(): void {
    this.userDataForm.patchValue({
      billingAddress: {
        city: this.userBilling?.city,
        street: this.userBilling?.street,
        number: this.userBilling?.number,
        floor: this.userBilling?.floor,
        door: this.userBilling?.door,
        country: this.userBilling?.country,
        postcode: this.userBilling?.postcode
      },
    })
  }
}
