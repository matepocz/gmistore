import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ProductFilterOptions} from "../../../models/product/product-filter-options";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-filter-dialog',
  templateUrl: './filter-dialog.component.html',
})
export class FilterDialogComponent implements OnInit {

  minPrice: number;
  maxPrice: number;

  priceForm: FormGroup = this.fb.group({
    minimumPrice: [1, Validators.compose(
      [Validators.required, Validators.min(1)])],
    maximumPrice: [0, Validators.compose(
      [Validators.required, Validators.min(2)])]
  })

  constructor(private fb: FormBuilder, public dialogRef: MatDialogRef<FilterDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: {
                filterOptions: ProductFilterOptions,
                deals: boolean
              }) {
    this.minPrice = data.filterOptions.minPrice;
    this.maxPrice = data.filterOptions.maxPrice;
    this.priceForm.patchValue({
      maximumPrice: data.filterOptions.maxPrice
    })
  }

  ngOnInit(): void {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  updateMinPrice() {
    this.data.filterOptions.minPrice = this.priceForm.get('minimumPrice').value;
  }

  updateMaxPrice() {
    this.data.filterOptions.maxPrice = this.priceForm.get('maximumPrice').value;
  }
}
