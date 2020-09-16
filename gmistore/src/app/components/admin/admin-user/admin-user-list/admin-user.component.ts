import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {AdminService} from "../../../../service/admin.service";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {MatTableDataSource} from "@angular/material/table";
import {Router} from "@angular/router";
import {MatSlideToggleChange} from "@angular/material/slide-toggle";
import {UserIsActiveModel} from "../../../../models/userIsActiveModel";
import {UserListDetailsModel} from "../../../../models/UserListDetailsModel";
import {AuthService} from "../../../../service/auth-service";
import {SubSink} from "subsink";

@Component({
  selector: 'app-admin-user',
  templateUrl: './admin-user.component.html',
  styleUrls: ['./admin-user.component.css']
})
export class AdminUserComponent implements OnInit, OnDestroy {
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  displayedColumns: string[] = ['id', 'username', 'email', 'roles', 'mail', 'active', 'edit'];
  userData: Array<UserListDetailsModel>;
  dataSource: MatTableDataSource<UserListDetailsModel>;
  myVariableColor: any;
  subs = new SubSink();

  constructor(private adminService: AdminService,
              private authService: AuthService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.getAllUsers();
  }

  getAllUsers() {
    this.subs.add(this.adminService.getUserList().subscribe(
      (data) => this.userData = data,
      error => console.log(error),
      () => {
        this.dataSource = new MatTableDataSource(this.userData);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      }
    ));
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  editUser(id: string) {
    this.router.navigate(['/admin/user/edit', id])
  }

  onChange(value: MatSlideToggleChange, id: number) {
    let userIsActiveData: UserIsActiveModel = new class implements UserIsActiveModel {
      active: boolean = value.checked;
      id: number = id;
    }();

    this.subs.add(this.adminService.setUserActivity(userIsActiveData).subscribe(
      () => console.log("Activity" + value.checked),
      (err) => console.log(err)
    ));
  }

  sendResetPassword(email: string) {
    this.subs.add(this.authService.sendResetMail(email).subscribe(
      () => this.myVariableColor = 'green',
      error => this.myVariableColor = 'red'
      )
    )
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }
}
