import {Component, Input, OnInit} from '@angular/core';
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  @Input() userDataForm: FormGroup;
  @Input() data: any;

  constructor() { }

  ngOnInit(): void {
    this.userDataForm.patchValue({
      username: this.data.username,
      firstName: this.data.firstName,
      lastName: this.data.lastName,
      email: this.data.email,
      phoneNumber: this.data.phoneNumber,
      registered: this.getDate(this.data.registered),
      active: this.data.active,
    });
  }

  getDate = (d: Date) => {
    let dt = new Date(d);
    let dtm = dt.getMonth();
    let dty = dt.getFullYear();
    let day = dt.getDay();
    return dty + "/" + dtm + "/" + day
  }

}
