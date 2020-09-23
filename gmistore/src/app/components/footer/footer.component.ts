import {Component, NgZone, OnInit, ViewChild} from '@angular/core';
import {CdkTextareaAutosize} from "@angular/cdk/text-field";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {EmailSendingService} from "../../service/email-sending.service";
import {errorHandler} from "../../utils/error-handler";

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})

export class FooterComponent implements OnInit {
  contactForm:FormGroup;

  constructor(private _ngZone: NgZone, private formBuilder: FormBuilder, private emailService: EmailSendingService) {
  }

  @ViewChild('autosize') autosize: CdkTextareaAutosize;

  ngOnInit(): void {
    this.contactForm = this.formBuilder.group({
      email: [null,[Validators.required, Validators.email]],
      subject: [null, [Validators.required]],
      message: [null, [Validators.minLength(5), Validators.maxLength(200)]]
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

      });
  }

}



