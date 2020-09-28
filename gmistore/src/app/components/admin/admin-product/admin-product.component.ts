import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {AdminService} from "../../../service/admin.service";
import {Subscription} from "rxjs";
import {ProductTableModel} from "../../../models/product/productTableModel";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {Router} from "@angular/router";

@Component({
  selector: 'app-admin-product',
  templateUrl: './admin-product.component.html',
  styleUrls: ['./admin-product.component.css']
})
export class AdminProductComponent implements OnInit, OnDestroy {
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  private subs: Subscription[] = [];
  productTableData: Array<ProductTableModel>;
  dataSource: MatTableDataSource<ProductTableModel>;
  displayedColumns: string[] = ['pic', 'name', 'category', 'price', 'active'];

  constructor(private adminService: AdminService,
              private route: Router) {
  }

  ngOnInit(): void {
    this.subs.push(
      this.adminService.fetchProductsTableData().subscribe(
        data => {
          this.productTableData = data;
          this.dataSource = new MatTableDataSource<ProductTableModel>(this.productTableData)
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        },
        error =>{
          console.log("Error:");
          console.log(error);
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

  ngOnDestroy() {
    this.subs.map(subs => subs.unsubscribe());
  }

  editProduct(slug: any) {
    this.route.navigate(['/edit-product', slug]);
  }
}
