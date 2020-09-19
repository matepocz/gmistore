import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'loading-spinner',
  templateUrl: './loading-spinner.component.html',
})
export class LoadingSpinnerComponent {

  constructor(public dialogRef: MatDialogRef<LoadingSpinnerComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {}
}
