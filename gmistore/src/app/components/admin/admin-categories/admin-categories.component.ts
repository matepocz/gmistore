import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {AdminService} from "../../../service/admin.service";
import {MainCategoryModel} from "../../../models/main-category.model";
import {Subscription} from "rxjs";
import {FormBuilder, FormGroupDirective, Validators} from "@angular/forms";
import {NewMainCategoryModel} from "../../../models/new-main-category.model";
import {errorHandler} from "../../../utils/error-handler";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";

@Component({
  selector: 'app-admin-categories',
  templateUrl: './admin-categories.component.html',
  styleUrls: ['./admin-categories.component.css']
})
export class AdminCategoriesComponent implements OnInit, OnDestroy {

  @ViewChild(FormGroupDirective) mainCategoryDirective: FormGroupDirective;

  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'bottom';

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
  createMainCategorySub: Subscription;

  constructor(private adminService: AdminService, private formBuilder: FormBuilder,
              private snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.fetchCategories();
  }

  fetchCategories() {
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
    this.adminService.createMainCategory(this.newMainCategory).subscribe(
      (response) => {
        if (response) {
          this.openSnackBar("Kategória hozzáadva!");
          this.fetchCategories();
          this.mainCategoryDirective.resetForm();
        } else {
          this.openSnackBar("Valami hiba történt!");
        }
      }, (error) => {
        console.log(error);
        errorHandler(error, this.categoryForm);
      }
    )
  }

  openSnackBar(message: string) {
    this.snackBar.open(message, 'OK', {
      duration: 2000,
      horizontalPosition: this.horizontalPosition,
      verticalPosition: this.verticalPosition,
    });
  }

  ngOnDestroy() {
    this.categoriesSub.unsubscribe();
    if (this.createMainCategorySub) {
      this.createMainCategorySub.unsubscribe();
    }
  }
}
