import {AfterViewChecked, ChangeDetectorRef, Component, OnInit} from '@angular/core';
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

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit , AfterViewChecked{
  userForm: FormGroup;
  details: any[];
  user: UserModel;
  loaded: boolean = false;
  roleOptions: Array<RolesInitModel>;
  toSend: any;

  constructor(private sharingService: SharingService,
              private fb: FormBuilder, private route: ActivatedRoute,
              private adminService: AdminService,
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
      registered: ['', Validators.required],
      active: ['', Validators.required],

    })
  }


  ngOnInit(): void {
    this.route.paramMap.subscribe(
      paramMap => {
        const editableId = paramMap.get('id');
        if (editableId) {
          this.getUserDetails(editableId);
        }
      },
      error => console.warn(error),
    );
  }

  ngAfterViewChecked() {
    this.cdRef.detectChanges();
  }

  getUserDetails(id) {
    this.adminService.getInitRoles().subscribe(
      rolesData => {
        this.roleOptions = rolesData
      })

    this.adminService.getAccount(id).subscribe(
      data => {
        console.log(data);
        this.user = data;
        this.sharingService.nextMessage(data);
      },
      error => console.log(error),
      () => this.loaded = true
    )
  }

  onSubmit(id:number) {
    const data: UserEditableDetailsByAdmin = {...this.userForm.value};
    data.roles = this.toSend;
    data.id = id;
    this.updateUser(data);
  }

  private updateUser(data: UserEditableDetailsByAdmin) {
    console.log(data)
    this.adminService.updateUser(data).subscribe(
      () => this.router.navigate(['/admin/user']),
      error => errorHandler(error, this.userForm),
    );
  }
}


