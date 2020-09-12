import {Component, OnDestroy, OnInit} from '@angular/core';
import {AdminService} from "../../../service/admin.service";
import {MainCategoryModel} from "../../../models/main-category.model";
import {Subscription} from "rxjs";
import {FormBuilder, Validators} from "@angular/forms";
import {NewMainCategoryModel} from "../../../models/new-main-category.model";

@Component({
  selector: 'app-admin-categories',
  templateUrl: './admin-categories.component.html',
  styleUrls: ['./admin-categories.component.css']
})
export class AdminCategoriesComponent implements OnInit, OnDestroy {

  categories: Array<MainCategoryModel>;

  newMainCategory: NewMainCategoryModel;

  categoryForm = this.formBuilder.group({
    key: [null, Validators.compose(
      [Validators.required, Validators.minLength(3), Validators.maxLength(30)])],
    displayName: [null, Validators.compose(
      [Validators.required, Validators.minLength(3), Validators.maxLength(30)])],
    isActive: [true]
  });

  categoriesSub: Subscription;

  constructor(private adminService: AdminService, private formBuilder: FormBuilder) {
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

  onCategorySubmit() {
    this.newMainCategory = this.categoryForm.value;
  }

  ngOnDestroy() {
    this.categoriesSub.unsubscribe();
  }
}
