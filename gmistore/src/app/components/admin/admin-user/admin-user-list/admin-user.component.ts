import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Subscription} from "rxjs";
import {AdminService} from "../../../../service/admin.service";
import {UserModel} from "../../../../models/user-model";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {MatTableDataSource} from "@angular/material/table";

@Component({
  selector: 'app-admin-user',
  templateUrl: './admin-user.component.html',
  styleUrls: ['./admin-user.component.css']
})
export class AdminUserComponent implements OnInit, OnDestroy {
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  displayedColumns: string[] = ['id', 'username', 'email', 'roles', 'active'];
  subscription: Subscription;
  private userData: Array<UserModel>;
  dataSource: MatTableDataSource<UserModel>;

  constructor(private adminService: AdminService) {
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
}
