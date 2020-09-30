import {ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {EmailModel} from "../../../models/messages/email-model";
import {Subscription} from "rxjs";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {LoadingSpinnerComponent} from "../../loading-spinner/loading-spinner.component";
import {SpinnerService} from "../../../service/spinner-service.service";
import {EmailSendingService} from "../../../service/email-sending.service";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {FormBuilder, FormGroup, FormGroupDirective, Validators} from "@angular/forms";
import {PopupSnackbar} from "../../../utils/popup-snackbar";
import {errorHandler} from "../../../utils/error-handler";
import {ReplyEmailModel} from "../../../models/messages/reply-email-model";
import {ConfirmDialog} from "../../confirm-delete-dialog/confirm-dialog";
import {EmailTableModel} from "../../../models/messages/email-table-model";

@Component({
  selector: 'app-email-management',
  templateUrl: './admin-email-management.html',
  styleUrls: ['./admin-email-management.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class AdminEmailManagement implements OnInit, OnDestroy {
  @ViewChild(FormGroupDirective) messageForm: FormGroupDirective;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  contactForm: FormGroup;
  emailsSub: Subscription = new Subscription();
  emailTableData: Array<EmailTableModel>
  dataSource: MatTableDataSource<EmailTableModel>;
  displayedColumns: string[] = ['targy', 'email', 'datum', 'reszlet', 'torles'];

  numberOfEmails = 0;
  pageIndex: number = 0;
  pageSize: number = 10;
  pageSizeOptions: Array<number> = [10, 20, 50];

  replyEmail: ReplyEmailModel;
  spinner: MatDialogRef<LoadingSpinnerComponent>;


  constructor(private spinnerService: SpinnerService,
              private router: Router,
              private cdRef: ChangeDetectorRef,
              private emailService: EmailSendingService,
              private activatedRoute: ActivatedRoute,
              private snackbar: PopupSnackbar,
              private dialog: MatDialog,
              private formBuilder: FormBuilder) {
  }


  ngOnInit(): void {

    this.contactForm = this.formBuilder.group({
      message: [null, Validators.compose(
        [Validators.required, Validators.minLength(0), Validators.maxLength(2000)])]
    });
    this.emailsSub.add(
      this.activatedRoute.queryParamMap.subscribe(
        (param: ParamMap) => {
          this.pageSize = Number(param.get('pageSize'));
          this.pageIndex = Number(param.get('pageIndex'));
          this.creatingAllActiveEmailsTable();
        }
      )
    );

  }


  private creatingAllActiveEmailsTable() {
    this.spinner = this.spinnerService.start();
    this.emailsSub.add(
      this.emailService.getAllActiveIncomeEmails(this.pageSize,this.pageIndex).subscribe(
        response=> {
          console.log(response)
          this.emailTableData = response.emails;
          this.dataSource = new MatTableDataSource<EmailTableModel>(this.emailTableData);
          this.numberOfEmails = response.totalElements;
          this.spinnerService.stop(this.spinner);
        }, error => {
          this.spinnerService.stop(this.spinner);
        }, () => {
          this.spinnerService.stop(this.spinner);
        }
      )
    );
  }



  sendReplyEmail(email: EmailModel) {
    this.replyEmail = new ReplyEmailModel();
    this.replyEmail.id = email.id;
    this.replyEmail.email = email.email
    this.replyEmail.subject = email.subject;
    this.replyEmail.message = this.contactForm.get('message').value;
    this.replyEmail.active = true;

    this.emailsSub.add(
      this.emailService.sendReplyEmailToUser(this.replyEmail).subscribe((response: boolean) => {
          if (response) {
            this.snackbar.popUp('Az üzenet sikeresen elküldve');
          }
        }, error => {
          errorHandler(error, this.contactForm);
          console.warn(error);
        }, () => {
          this.messageForm.resetForm();
        }
      )
    );

  }

  openDeleteEmailDialog(emailId: number) {
    const dialogRef = this.dialog.open(ConfirmDialog, {
      width: '250px',
      data: {
        message: 'Biztosan törölni szeretnéd?',
      }
    });

    this.emailsSub.add(
      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.deleteEmail(emailId);
        }
      })
    );
  }

  deleteEmail(id: number) {
    this.emailsSub.add(this.emailService.deleteEmail(id).subscribe((response: boolean) => {
        if (response) {
          this.snackbar.popUp('Az üzenet sikeresen törölve')
          this.creatingAllActiveEmailsTable();
        }
      }, error => {
        this.snackbar.popUp("Valami hiba történt!");
        console.warn(error);
      }, () => {
      }
    ));
  }


  ngOnDestroy(): void {
    this.emailsSub.unsubscribe();
  }

  detectChanges() {
    this.cdRef.detectChanges();
  }

  paginationEventHandler($event: PageEvent) {
    this.pageSize = $event.pageSize;
    this.pageIndex = $event.pageIndex;
    this.router.navigate(['.'], {
      relativeTo: this.activatedRoute,
      queryParams: {
        pageSize: this.pageSize,
        pageIndex: this.pageIndex,
      }
    });
  }
}
