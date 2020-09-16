import {AfterViewChecked, ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {UserService} from "../../../service/user.service";
import {UserModel} from "../../../models/user-model";
import {AddressModel} from "../../../models/address-model";
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {OrderModel} from "../../../models/order-model";
import {RolesInitModel} from "../../../models/rolesInitModel";
import {SharingService} from "../../../service/sharing.service";
import {AdminService} from "../../../service/admin.service";
import {UserEditableDetailsByAdmin} from "../../../models/userEditableDetailsByAdmin";
import {errorHandler} from "../../../utils/error-handler";
import {SubSink} from "subsink";
import {UserEditableDetailsByUser} from "../../../models/userEditableDetailsByUser";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit ,OnDestroy, AfterViewChecked{
  userForm: FormGroup;
  details: any[];
  user: UserModel;
  loaded: boolean = false;
  subs = new SubSink();

  constructor(private sharingService: SharingService,
              private fb: FormBuilder, private route: ActivatedRoute,
              private adminService: AdminService,
              private userService: UserService,
              private router: Router, private cdRef: ChangeDetectorRef) {

    this.userForm = this.fb.group({
      shippingAddress: this.fb.group({
        city: [''],
        street: [''],
        number: ['',Validators.pattern("^[0-9]*$")],
        floor: [''],
        door: ['',Validators.pattern("^[0-9]*$")],
        country: [''],
        postcode: ['',Validators.pattern("^[0-9]*$")]
      }),
      billingAddress: this.fb.group({
        city: [''],
        street: [''],
        number: ['',Validators.pattern("^[0-9]*$")],
        floor: ['',Validators.pattern("^[0-9]*$")],
        door: ['',Validators.pattern("^[0-9]*$")],
        country: [''],
        postcode: ['',Validators.pattern("^[0-9]*$")]
      }),
      username: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      phoneNumber: [''],
    })
  }

  ngOnInit(): void {
    this.getUserDetails();
  }

  ngAfterViewChecked() {
    this.cdRef.detectChanges();
  }

  getUserDetails() {
    this.subs.add(this.userService.getUser().subscribe(
      data => {
        console.log(data);
        this.user = data;
        this.sharingService.nextMessage(data);
      },
      error => console.log(error),
      () => this.loaded = true
    ));
  }

  onSubmit() {
    const data: UserEditableDetailsByUser = {...this.userForm.value};
    this.updateUser(data);
  }

  private updateUser(data: UserEditableDetailsByUser) {
    console.log(data)
    this.subs.add(this.userService.updateUser(data).subscribe(
      () => this.router.navigate(['']),
      error => errorHandler(error, this.userForm),
    ));
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }
}


