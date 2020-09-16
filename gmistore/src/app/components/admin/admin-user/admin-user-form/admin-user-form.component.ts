import {AfterViewChecked, ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {UserModel} from "../../../../models/user-model";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AdminService} from "../../../../service/admin.service";
import {SharingService} from "../../../../service/sharing.service";
import {RolesInitModel} from "../../../../models/rolesInitModel";
import {errorHandler} from "../../../../utils/error-handler";
import {UserEditableDetailsByAdmin} from "../../../../models/userEditableDetailsByAdmin";
import {SubSink} from "subsink";

@Component({
  selector: 'app-admin-user-form',
  templateUrl: './admin-user-form.component.html',
  styleUrls: ['./admin-user-form.component.css']
})
export class AdminUserFormComponent implements OnInit ,OnDestroy, AfterViewChecked {
  userForm: FormGroup;
  details: any[];
  user: UserModel;
  loaded: boolean = false;
  roleOptions: Array<RolesInitModel>;
  toSend: any;
  subs = new SubSink();

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
      roles: this.fb.array([], Validators.required),
      active: ['', Validators.required],

    })
  }


  ngOnInit(): void {
    this.subs.add(this.route.paramMap.subscribe(
      paramMap => {
        const editableId = paramMap.get('id');
        if (editableId) {
          this.getUserDetails(editableId);
        }
      },
      error => console.warn(error),
    ));
  }

  ngAfterViewChecked() {
    this.cdRef.detectChanges();
  }

  getUserDetails(id) {
    this.subs.add(this.adminService.getInitRoles().subscribe(
      rolesData => {
        this.roleOptions = rolesData
      }));

    this.subs.add(this.adminService.getAccount(id).subscribe(
      data => {
      console.log(data);
      this.user = data;
      this.sharingService.nextMessage(data);
    },
        error => console.log(error),
      () => this.loaded = true
    ));
  }

  onSubmit(id:number) {
    const data: UserEditableDetailsByAdmin = {...this.userForm.value};
    data.roles = this.toSend;
    data.id = id;
    this.updateUser(data);
  }

  private updateUser(data: UserEditableDetailsByAdmin) {
    console.log(data)
    this.subs.add(this.adminService.updateUser(data).subscribe(
      () => this.router.navigate(['/admin/user']),
      error => errorHandler(error, this.userForm),
    ));
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }
}

