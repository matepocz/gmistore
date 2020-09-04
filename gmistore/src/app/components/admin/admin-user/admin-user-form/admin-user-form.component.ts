import {Component, OnInit} from '@angular/core';
import {UserModel} from "../../../../models/user-model";
import {ActivatedRoute} from "@angular/router";
import { FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AdminService} from "../../../../service/admin.service";
import {SharingService} from "../../../../service/sharing.service";
import {RolesFormModel} from "../../../../models/rolesInitModel";

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
  roleOptions: Array<RolesFormModel>;

  constructor(private sharingService: SharingService, private fb: FormBuilder, private route: ActivatedRoute, private adminService: AdminService) {
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
    console.log(roles);
    return this.roleOptions.map(
      role => {
        console.log(role.name);
        return roles.includes(role.name);
      });
  }

  private createRolesToSend(): string[] {
    return this.userForm.value.roles
      .map((role, index) => role ? this.roleOptions[index] : null)
      .filter(weapon => weapon !== null);
  }
}
