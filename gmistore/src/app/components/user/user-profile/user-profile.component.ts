import {Component, OnInit} from '@angular/core';
import {UserService} from "../../../service/user.service";
import {UserModel} from "../../../models/user-model";
import {AddressModel} from "../../../models/address-model";
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";
import {FormArray, FormControl, FormGroup, Validators} from "@angular/forms";
import {OrderModel} from "../../../models/order-model";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  user: UserModel;
  deliveryAddress: AddressModel;

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.userService.getUser().subscribe(data => {
      console.log(data)
      this.user = data;
      this.deliveryAddress = data.shippingAddress;
    }, error => {
      console.log(error);
    })
  }
}
