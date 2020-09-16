import {Component, Inject, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {AdminService} from "../../../service/admin.service";
import {MainCategoryModel} from "../../../models/main-category.model";
import {Subscription} from "rxjs";
import {FormBuilder, FormGroupDirective, Validators} from "@angular/forms";
import {NewCategoryModel} from "../../../models/new-category.model";
import {errorHandler} from "../../../utils/error-handler";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";

export interface DialogData {
  name: string;
  key: string;
}

@Component({
  selector: 'delete-dialog',
  templateUrl: 'delete-dialog.html',
})

export class DeleteDialog {
  constructor(
    public dialogRef: MatDialogRef<DeleteDialog>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}

@Component({
  selector: 'app-admin-categories',
  templateUrl: './admin-categories.component.html',
  styleUrls: ['./admin-categories.component.css']
})

export class AdminCategoriesComponent implements OnInit, OnDestroy {

  @ViewChild(FormGroupDirective) categoryForm: FormGroupDirective;

  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'bottom';

  categories: Array<MainCategoryModel>;

  newCategoryModel: NewCategoryModel;

  mainCategoryForm = this.formBuilder.group({
    key: [null, Validators.compose(
      [Validators.required, Validators.minLength(3), Validators.maxLength(30)])],
    displayName: [null, Validators.compose(
      [Validators.required, Validators.minLength(3), Validators.maxLength(30)])],
    icon: [null, Validators.compose([Validators.maxLength(50)])],
    isActive: [true],
  });

  subCategoryForm = this.formBuilder.group({
    key: [null, Validators.compose(
      [Validators.required, Validators.minLength(3), Validators.maxLength(30)])],
    displayName: [null, Validators.compose(
      [Validators.required, Validators.minLength(3), Validators.maxLength(30)])],
    icon: [null, Validators.compose([Validators.maxLength(50)])],
    isActive: [true],
    mainCategoryKey: [null]
  });

  categoriesSub: Subscription;
  createMainCategorySub: Subscription;
  createSubCategorySub: Subscription;

  constructor(private adminService: AdminService, private formBuilder: FormBuilder,
              private snackBar: MatSnackBar, public dialog: MatDialog) {
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

  onMainCategorySubmit() {
    this.newCategoryModel = this.mainCategoryForm.value;
    this.newCategoryModel.isSubCategory = false;
    this.createMainCategorySub = this.adminService.createNewProductCategory(this.newCategoryModel).subscribe(
      (response: boolean) => {
        if (response) {
          this.openSnackBar("Kategória hozzáadva!");
          this.fetchCategories();
          this.categoryForm.resetForm();
        } else {
          this.openSnackBar("Valami hiba történt!");
        }
      }, (error) => {
        console.log(error);
        errorHandler(error, this.mainCategoryForm);
      }
    )
  }

  onSubCategorySubmit() {
    this.newCategoryModel = this.subCategoryForm.value;
    this.newCategoryModel.isSubCategory = true;
    this.createSubCategorySub = this.adminService.createNewProductCategory(this.newCategoryModel).subscribe(
      (response: boolean) => {
        if (response) {
          this.openSnackBar("Kategória hozzáadva!");
          this.fetchCategories();
          this.categoryForm.resetForm();
        } else {
          this.openSnackBar("Valami hiba történt!");
        }
      }, (error) => {
        errorHandler(error, this.subCategoryForm);
        console.log(error);
      }
    )
  }

  setCategoryInactive(key: string) {
    this.adminService.setCategoryInactive(key).subscribe(
      (response) => {
        if (response) {
          this.openSnackBar("Kategória inaktív!")
          this.fetchCategories();
        } else {
          this.openSnackBar("Nincs jogosultságod a művelethez!")
        }
      }, () => {
        this.openSnackBar("Valami hiba történt!");
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

  openDialog(category: string, key: string): void {
    const dialogRef = this.dialog.open(DeleteDialog, {
      width: '250px',
      data: {name: category}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.setCategoryInactive(key);
      }
    });
  }

  ngOnDestroy() {
    this.categoriesSub.unsubscribe();
    if (this.createMainCategorySub) {
      this.createMainCategorySub.unsubscribe();
    }
    if (this.createSubCategorySub) {
      this.createSubCategorySub.unsubscribe();
    }
  }
}
