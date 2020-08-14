import {Component, OnInit} from '@angular/core';
import {UserService} from "../../service/user.service";
import {UserModel} from "../../models/user-model";
import {AddressModel} from "../../models/address-model";

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
      this.deliveryAddress = data.address;
    }, error => {
      console.log(error);
    })
  }

}
