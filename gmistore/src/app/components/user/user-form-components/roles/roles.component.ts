import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormArray, FormControl, FormGroup} from "@angular/forms";
import {RolesInitModel} from "../../../../models/rolesInitModel";

@Component({
  selector: 'app-roles',
  templateUrl: './roles.component.html',
  styleUrls: ['./roles.component.css']
})
export class RolesComponent implements OnInit {
  @Input() userDataForm: FormGroup;
  @Input() userRolesData: any;
  @Input() roleOptions: Array<RolesInitModel>;
  @Input() roles: any[];
  @Output() rolesToSend : EventEmitter<any>= new EventEmitter<any>();

  constructor() { }

  ngOnInit(): void {
    this.userDataForm.patchValue({
      roles: this.createRolesFormArray(this.roles)
    })

    this.rolesToSend.emit(this.createRolesToSend());
  }

  getRolesFormArray = () => {
    return this.userDataForm.get("roles") as FormArray;
  };

  private createRolesFormArray = (roles: Array<string>) => {
    let tempRoll = this.getRolesFormArray();

    return this.roleOptions.map(
      role => {
        tempRoll.push(new FormControl(roles.includes(role.name)));
        return roles.includes(role.name);
      });
  }

  createRolesToSend = () => {
    return this.userDataForm.value.roles
      .map((role, index) => role ? this.roleOptions[index].name : null)
      .filter(role => role !== null);
  }

}
