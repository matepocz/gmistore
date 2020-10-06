import {Component, OnDestroy, OnInit} from '@angular/core';
import {MessageService} from "../../service/message.service";
import {Title} from "@angular/platform-browser";
import {MessageDetailsModel} from "../../models/messages/message-details.model";
import {Subscription} from "rxjs";
import {MessagesResponseModel} from "../../models/messages/messages-response.model";
import {SpinnerService} from "../../service/spinner-service.service";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {LoadingSpinnerComponent} from "../loading-spinner/loading-spinner.component";
import {SideNavComponent} from "../side-nav/side-nav.component";
import {ConfirmDialog} from "../confirm-delete-dialog/confirm-dialog";
import {PopupSnackbar} from "../../utils/popup-snackbar";
import {MessageDialogComponent} from "../message-dialog/message-dialog.component";

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit, OnDestroy {

  spinner: MatDialogRef<LoadingSpinnerComponent> = this.spinnerService.start();

  incomingMessages: Array<MessageDetailsModel>;
  outgoingMessages: Array<MessageDetailsModel>;
  unreadIncomingCount: number = 0;

  subscriptions: Subscription = new Subscription();

  constructor(private messageService: MessageService, private titleService: Title,
              private spinnerService: SpinnerService, private sideNav: SideNavComponent,
              private dialog: MatDialog, private snackbar: PopupSnackbar) {
  }

  ngOnInit(): void {
    this.titleService.setTitle("Üzeneteim - GMI Store");
    this.fetchMessages();
  }

  private fetchMessages() {
    this.subscriptions.add(
      this.messageService.getMessages().subscribe(
        (response: MessagesResponseModel) => {
          this.incomingMessages = response.incoming;
          this.outgoingMessages = response.outgoing;
          this.countUnreadIncomingMessages();
          this.spinnerService.stop(this.spinner);
        }, (error) => {
          console.log(error);
          this.spinnerService.stop(this.spinner);
        }
      )
    );
  }

  setMessageRead(message: MessageDetailsModel) {
    if (!message.read) {
      this.markMessageRead(message.id);
      this.sideNav.updateCountOfUnreadMails(1);
      this.countUnreadIncomingMessages();
    }
    message.read = true;
  }

  markMessageRead(id: number) {
    this.subscriptions.add(
      this.messageService.markMessageRead(id).subscribe(
        (response) => {
        }, (error) => {
          console.log(error);
        }
      )
    );
  }

  countUnreadIncomingMessages() {
    let count = 0;
    this.incomingMessages.forEach(
      (message) => {
        if (!message.read) {
          count++;
        }
      }
    );
    this.unreadIncomingCount = count;
  }

  formatMessageSentDate(date: Date): string {
    let dateString = new Intl.DateTimeFormat('hu-HU',
      {year: 'numeric', month: 'long', day: '2-digit', hour: 'numeric', minute: 'numeric'})
      .format(new Date(date)).toString();
    return dateString.replace(',', ' ');
  }

  openMessageDeleteDialog(message: MessageDetailsModel) {
    const dialogRef = this.dialog.open(ConfirmDialog, {
      width: '250px',
      data: {
        message: 'Biztosan törölni szeretnéd?',
        name: message.subject
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleteMessage(message);
      }
    });
  }

  openNewMessageDialog(receiver: string) {
    const dialogRef = this.dialog.open(MessageDialogComponent, {
      width: '90%',
      data: {
        receiver: receiver,
      }
    });

    this.subscriptions.add(
      dialogRef.afterClosed().subscribe(message => {
        if (message) {
          this.spinner = this.spinnerService.start();
          this.subscriptions.add(
            this.messageService.createMessage(message).subscribe(
              (response) => {
                if (response) {
                  this.snackbar.popUp("Üzenet sikeresen elküldve!");
                }
                this.spinnerService.stop(this.spinner);
                this.fetchMessages();
              }, (error) => {
                console.log(error);
                this.spinnerService.stop(this.spinner);
              }
            )
          );
        }
      })
    );
  }

  deleteMessage(message: MessageDetailsModel) {
    this.spinner = this.spinnerService.start();
    this.subscriptions.add(
      this.messageService.deleteMessage(message.id).subscribe(
        (response: boolean) => {
          if (response) {
            this.countUnreadIncomingMessages();
            this.snackbar.popUp("Sikeres törlés!");
            this.fetchMessages();
          }
          this.spinnerService.stop(this.spinner);
        }, (error) => {
          console.log(error);
          this.spinnerService.stop(this.spinner);
        }
      )
    );
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}
