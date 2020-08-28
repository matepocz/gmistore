import {Component, OnInit} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {GdprDialogComponent} from "./components/gdpr-dialog/gdpr-dialog.component";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'GMI store';
  isView: string = 'false';

  constructor(private  dialog: MatDialog) {
  }


  ngOnInit() {
    if (localStorage.getItem('isView') !== 'true') {
      setTimeout(() => {
          this.openDialog();
          this.isView = 'true';
      }, 2000);
      localStorage.setItem('isView','true');
    }

  }

  openDialog() {
    this.dialog.open(GdprDialogComponent);
  }

}
