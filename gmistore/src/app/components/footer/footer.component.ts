import {Component, NgZone, OnInit, ViewChild} from '@angular/core';
import {CdkTextareaAutosize} from "@angular/cdk/text-field";
import {FormBuilder, FormGroup, FormGroupDirective, Validators} from "@angular/forms";
import {EmailSendingService} from "../../service/email-sending.service";
import {errorHandler} from "../../utils/error-handler";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";


@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})

export class FooterComponent implements OnInit {
  contactForm: FormGroup;
  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'bottom';

  constructor(private _ngZone: NgZone,
              private formBuilder: FormBuilder,
              private emailService: EmailSendingService,
              private snackBar: MatSnackBar) {
  }

  @ViewChild(FormGroupDirective) messageForm: FormGroupDirective;
  @ViewChild('autosize') autosize: CdkTextareaAutosize;

  ngOnInit(): void {
    this.contactForm = this.formBuilder.group({
      email: [null, Validators.compose([Validators.required, Validators.email])],
      subject: [null, Validators.compose(
        [Validators.required,Validators.minLength(3), Validators.maxLength(30)])],
      message: [null, Validators.compose(
        [Validators.required,Validators.minLength(3), Validators.maxLength(1000)])]
    });
  }

  openSnackBar(message: string) {
    this.snackBar.open(message, 'OK', {
      duration: 2000,
      horizontalPosition: this.horizontalPosition,
      verticalPosition: this.verticalPosition,
    });
  }

  submitContactForm() {
    let emailData = this.contactForm.value;
    this.emailService.saveEmailFromUser(emailData)
      .subscribe(response => {
        console.log(response)
      }, error => {
        errorHandler(error, this.contactForm);
      }, () => {
        this.messageForm.resetForm();
        this.openSnackBar('Az üzenet sikeresen elküldve');
      });
  }

}



