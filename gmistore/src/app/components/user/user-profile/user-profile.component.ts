import {AfterViewChecked, ChangeDetectorRef, Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {UserService} from "../../../service/user.service";
import {UserModel} from "../../../models/user/user-model";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SharingService} from "../../../service/sharing.service";
import {AdminService} from "../../../service/admin.service";
import {errorHandler} from "../../../utils/error-handler";
import {SubSink} from "subsink";
import {UserEditableDetailsByUser} from "../../../models/user/userEditableDetailsByUser";
import {OrderService} from "../../../service/order.service";
import {ProductOrderedListModel} from "../../../models/product/productOrderedListModel";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit, OnDestroy, AfterViewChecked {
  @ViewChild("orders") prop: ElementRef
  userForm: FormGroup;
  details: any[];
  user: UserModel;
  loaded: boolean = false;
  subs = new SubSink();
  orderedProducts: Array<ProductOrderedListModel>;
  show: boolean;

  constructor(private sharingService: SharingService,
              private fb: FormBuilder, private route: ActivatedRoute,
              private adminService: AdminService,
              private orderService: OrderService,
              private userService: UserService,
              private router: Router,
              private cdRef: ChangeDetectorRef) {

    this.userForm = this.fb.group({
      shippingAddress: this.fb.group({
        city: [''],
        street: [''],
        number: ['', Validators.pattern("^[0-9]*$")],
        floor: [''],
        door: ['', Validators.pattern("^[0-9]*$")],
        country: [''],
        postcode: ['', Validators.pattern("^[0-9]*$")]
      }),
      billingAddress: this.fb.group({
        city: [''],
        street: [''],
        number: ['', Validators.pattern("^[0-9]*$")],
        floor: ['', Validators.pattern("^[0-9]*$")],
        door: ['', Validators.pattern("^[0-9]*$")],
        country: [''],
        postcode: ['', Validators.pattern("^[0-9]*$")]
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
    this.getOrderedProducts();
  }

  ngAfterViewChecked() {
    this.cdRef.detectChanges();
  }

  getUserDetails() {
    this.subs.add(this.userService.getUser().subscribe(
      data => {
        console.log(data);
        this.user = data;
        console.log(data);
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

  private getOrderedProducts() {
    this.subs.add(this.orderService.getOrderItems().subscribe(
      (data) => this.orderedProducts = data
    ));
  }

  openClose() {
    this.show = !this.show;
    this.prop.nativeElement.scrollIntoView({ behavior: "smooth", block: "start" });
  }
}


