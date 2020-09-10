import {Component, OnDestroy, OnInit} from '@angular/core';
import {AdminService} from "../../../service/admin.service";
import {MainCategoryModel} from "../../../models/main-category.model";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-admin-categories',
  templateUrl: './admin-categories.component.html',
  styleUrls: ['./admin-categories.component.css']
})
export class AdminCategoriesComponent implements OnInit, OnDestroy {

  categories: Array<MainCategoryModel>;

  categoriesSub: Subscription;

  constructor(private adminService: AdminService) {
  }

  ngOnInit(): void {
    this.categoriesSub = this.adminService.getProductCategories().subscribe(
      (response: Array<MainCategoryModel>) => {
        this.categories = response;
      }, (error) => {
        console.log(error);
      }
    )
  }

  ngOnDestroy() {
    this.categoriesSub.unsubscribe();
  }

}
