import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

export interface DialogData {
  message?: string,
  name?: string
}

@Component({
  selector: 'app-confirm-delete-dialog',
  templateUrl: './confirm-dialog.html',
})
export class ConfirmDialog implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<ConfirmDialog>,
    @Inject(MAT_DIALOG_DATA) public data?: DialogData) {
  }

  ngOnInit(): void {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
