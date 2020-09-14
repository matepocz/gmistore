import {Component, OnInit} from '@angular/core';
import {UserModel} from "../../../../models/user-model";
import {ActivatedRoute, Router} from "@angular/router";
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {AdminService} from "../../../../service/admin.service";
import {SharingService} from "../../../../service/sharing.service";
import {RolesInitModel} from "../../../../models/rolesInitModel";
import {errorHandler} from "../../../../utils/error-handler";
import {UserEditableDetailsByAdmin} from "../../../../models/userEditableDetailsByAdmin";

@Component({
  selector: 'app-admin-user-form',
  templateUrl: './admin-user-form.component.html',
  styleUrls: ['./admin-user-form.component.css']
})
export class AdminUserFormComponent implements OnInit {
  userForm: FormGroup;
  details: any[];
  user: UserModel;
  loaded: boolean = false;
  roleOptions: Array<RolesInitModel>;

  constructor(private sharingService: SharingService,
              private fb: FormBuilder, private route: ActivatedRoute,
              private adminService: AdminService,
              private router: Router) {

    this.userForm = this.fb.group({
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

  getRolesFormArray = () => {
    return this.userForm.get("roles") as FormArray;
  };

  getUserDetails(id) {
    this.adminService.getInitRoles().subscribe(
      data => {
        this.roleOptions = data
      })

    this.adminService.getAccount(id).subscribe(data => {
      console.log(data);
      this.user = data;
      this.sharingService.nextMessage(data);

      this.userForm.patchValue({
        username: data.username,
        firstName: data.firstName,
        lastName: data.lastName,
        email: data.email,
        phoneNumber: data.phoneNumber,
        roles: this.createRolesFormArray(data.roles),
        registered: this.getDate(data.registered),
        active: data.active,
      });
      this.loaded = true;
    }, error => console.log(error))

  }

  getDate = (d: Date) => {
    let dt = new Date(d);
    let dtm = dt.getMonth();
    let dty = dt.getFullYear();
    let day = dt.getDay();
    return dty + "/" + dtm + "/" + day
  }

  private createRolesFormArray = (roles: Array<string>) => {
    let tempRoll = this.getRolesFormArray();

    return this.roleOptions.map(
      role => {
        tempRoll.push(new FormControl(roles.includes(role.name)));
        return roles.includes(role.name);
      });
  }

  private createRolesToSend(): string[] {
    return this.userForm.value.roles
      .map((role, index) => role ? this.roleOptions[index].name : null)
      .filter(role => role !== null);
  }

  onSubmit(id:number) {
    const data: UserEditableDetailsByAdmin = {...this.userForm.value};
    data.roles = this.createRolesToSend();
    data.id = id;
    this.updateUser(data);
  }

  private updateUser(data: UserEditableDetailsByAdmin) {
    this.adminService.updateUser(data).subscribe(
      () => this.router.navigate(['/admin/user']),
      error => errorHandler(error, this.userForm),
    );
  }
}
