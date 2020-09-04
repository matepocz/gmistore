import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Subscription} from "rxjs";
import {AdminService} from "../../../../service/admin.service";
import {UserModel} from "../../../../models/user-model";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {MatTableDataSource} from "@angular/material/table";
import {Router} from "@angular/router";
import {SharingService} from "../../../../service/sharing.service";
import {MatSlideToggleChange} from "@angular/material/slide-toggle";
import {UserIsActiveModel} from "../../../../models/userIsActiveModel";

@Component({
  selector: 'app-admin-user',
  templateUrl: './admin-user.component.html',
  styleUrls: ['./admin-user.component.css']
})
export class AdminUserComponent implements OnInit, OnDestroy {
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  displayedColumns: string[] = ['id', 'username', 'email', 'roles', 'active', 'edit'];
  subscription: Subscription;
  userData: Array<UserModel>;
  dataSource: MatTableDataSource<UserModel>;

  constructor(private adminService: AdminService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.getAllUsers();
  }

  getAllUsers() {
    this.subscription = this.adminService.getUserList().subscribe(
      (data) => this.userData = data,
      error => console.log(error),
      () => {
        this.dataSource = new MatTableDataSource(this.userData);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      }
    )
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  editUser(id: string) {
    // let chosenUserData = this.userData.filter(u => u.id == id);
    // this.adminService.getAccount(+id).subscribe(
    //   (user: UserModel) => this.sharingService.nextMessage(user),
    //   err=> console.log(err),
    //   () =>  this.router.navigate(['/admin/user/edit',id])
    this.router.navigate(['/admin/user/edit', id])
  }

  onChange(value: MatSlideToggleChange, id: number) {
    let userIsActiveData: UserIsActiveModel = new class implements UserIsActiveModel {
      active: boolean = value.checked;
      id: number = id;
    }();

    this.adminService.setUserActivity(userIsActiveData).subscribe(
      () => console.log("Activity" + value.checked),
      (err) => console.log(err)
    )
  }
}
