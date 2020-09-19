import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {LoadingSpinnerComponent} from "../components/loading-spinner/loading-spinner.component";

@Injectable()
export class SpinnerService {

  constructor(private router: Router, private dialog: MatDialog) {
  }

  start(message?: string): MatDialogRef<LoadingSpinnerComponent> {

    return this.dialog.open(LoadingSpinnerComponent, {
      disableClose: true,
      data: message == '' || message == undefined ? "Egy pillanat..." : message
    });
  };

  stop(ref: MatDialogRef<LoadingSpinnerComponent>) {
    ref.close();
  }
}
