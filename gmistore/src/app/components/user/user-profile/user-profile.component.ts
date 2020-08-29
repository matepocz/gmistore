import {Component, OnInit} from '@angular/core';
import {UserService} from "../../../service/user.service";
import {UserModel} from "../../../models/user-model";
import {AddressModel} from "../../../models/address-model";
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {OrderModel} from "../../../models/order-model";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  user: UserModel;
  details: any[];
  shippingAddress: any[];
  billingAddress: any[];
  titles = ['Keresztnév', 'Vezetéknév', 'Felhasználónév', 'Email', 'Telefon', 'Regisztrált'];
  address = ['Város', 'Utca', 'Emelet', 'Irányítószám', 'Ország'];


  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.getUserDetails();
  }

  getUserDetails() {
    this.userService.getUser().subscribe(data => {
      console.log(data)
      this.user = data;
      this.details = [data.firstName, data.lastName, data.username, data.email, data.phoneNumber, this.getDate(data.registered)]

      this.shippingAddress = [data.shippingAddress.city, data.shippingAddress.street, data.shippingAddress.floor
        , data.shippingAddress.postcode, data.shippingAddress.country];

      this.billingAddress = [data.billingAddress.city, data.billingAddress.street, data.billingAddress.floor
        , data.billingAddress.postcode, data.billingAddress.country];

    }, error => console.log(error))
  }

  getDate = (d: Date) => {
    let dt = new Date(d);
    let dtm = dt.getMonth();
    let dty = dt.getFullYear();
    let day = dt.getDay();
    return dty + "/" + dtm + "/" + day
  }

  getDate2(d: Date): any {
    let dt = new Date(d);
    let dtm = dt.getMonth();
    let dty = dt.getFullYear();
    return dtm + "/" + dty
  }
}


