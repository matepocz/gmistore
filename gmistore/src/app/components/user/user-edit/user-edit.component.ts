import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserModel} from "../../../models/user/user-model";
import {AddressModel} from "../../../models/address-model";
import {UserService} from "../../../service/user.service";

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})
export class UserEditComponent implements OnInit {

  userForm: FormGroup;
  user: UserModel;
  deliveryAddress: AddressModel;

  constructor(private fb: FormBuilder, private userService: UserService) {
    this.userForm = this.fb.group({
      //'username': new FormControl('', Validators.required),
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      shippingAddress: this.fb.group({
        city: [''],
        street: [''],
        number: [''],
        floor: [''],
        door: [''],
        country: [''],
        postcode: ['']
      }),
      billingAddress: this.fb.group({
        city: [''],
        street: [''],
        number: [''],
        floor: [''],
        door: [''],
        country: [''],
        postcode: ['']
      }),
      email: ['', Validators.required],
      phoneNumber: [''],
      //'roles': new FormArray([], Validators.required),
      //'registered': new FormControl(''),
      //'active': new FormControl(''),
      //'orderList': new FormControl(''),
    })
  }

  ngOnInit(): void {
    this.getUserDetails();
  }

  getUserDetails() {
    this.userService.getUser().subscribe(data => {
      console.log(data)
      this.user = data;
      this.deliveryAddress = data.shippingAddress;

      this.userForm.patchValue({
        //username: data.username,
        firstName: data.firstName,
        lastName: data.lastName,
        shippingAddress: {
          city: data.shippingAddress.city,
          street: data.shippingAddress.street,
          number: data.shippingAddress.number,
          floor: data.shippingAddress.floor,
          door: data.shippingAddress.door,
          country: data.shippingAddress.country,
          postcode: data.shippingAddress.postcode
        },
        billingAddress: {
          city: data.billingAddress.city,
          street: data.billingAddress.street,
          number: data.billingAddress.number,
          floor: data.billingAddress.floor,
          door: data.billingAddress.door,
          country: data.billingAddress.country,
          postcode: data.billingAddress.postcode
        },
        email: data.email,
        phoneNumber: data.phoneNumber,
        //roles: data.roles,
        //registered: data.registered,
        //active: data.active,
        //orderList: data.orderList
      });
    }, error => {
      console.log(error);
    })
  }
}
