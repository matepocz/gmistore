import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {EmailModel} from "../../../models/email-model";
import {Subscription} from "rxjs";
import {MatDialogRef} from "@angular/material/dialog";
import {LoadingSpinnerComponent} from "../../loading-spinner/loading-spinner.component";
import {SpinnerService} from "../../../service/spinner-service.service";
import {EmailSendingService} from "../../../service/email-sending.service";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {Router} from "@angular/router";

@Component({
  selector: 'app-admin-income-emails',
  templateUrl: './admin-income-emails.component.html',
  styleUrls: ['./admin-income-emails.component.css']
})
export class AdminIncomeEmailsComponent implements OnInit, OnDestroy {
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  displayedColumns: string[] = ['targy', 'email', 'datum','reszlet'];

  emails: Array<EmailModel>;
  private emailsSub: Subscription = new Subscription();
  spinner: MatDialogRef<LoadingSpinnerComponent> = this.spinnerService.start();

  dataSource: MatTableDataSource<EmailModel>;


  constructor(private spinnerService: SpinnerService,
              private router: Router,
              private emailService: EmailSendingService) {
  }


  ngOnInit(): void {
    this.emailsSub.add(
      this.emailService.getIncomeEmails().subscribe(
        (response) => {
          this.emails = response;
          console.log(response);
          this.spinnerService.stop(this.spinner);
        }, error => {
          this.spinnerService.stop(this.spinner);
        }, () => {
          this.dataSource = new MatTableDataSource(this.emails);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
          this.spinnerService.stop(this.spinner);
        }
      )
    );
  }


  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  ngOnDestroy(): void {
    this.emailsSub.unsubscribe();
  }

  jumpTheEmailDetails(id: string) {
    this.router.navigate(['admin/income-emails/edit/', id])
  }
}
