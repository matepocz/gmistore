import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../../service/auth-service";
import {Router} from "@angular/router";
import {emailValidator} from "../../../utils/email-validator";

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css']
})
export class PasswordResetComponent implements OnInit {
  resetPassForm: FormGroup;
  formValue: any;
  mailSent: boolean = false;
  mailNotSent: boolean = false;

  constructor(private formBuilder: FormBuilder,
              private authService:AuthService,
              private router:Router,
              ) {
    this.resetPassForm = this.formBuilder.group({
      email: [null,[Validators.required, Validators.email]]
    });
  }

  ngOnInit(): void {
  }

  onSubmit() {
    this.formValue = this.resetPassForm.value;
    this.authService.sendResetMail(this.formValue).subscribe(
      (r) => console.log(r),
      (error) => {
        this.mailNotSent = true;
        this.mailSent = false;
      },
      () => {
        this.mailSent = true;
        this.mailNotSent = false;
      }
    )
  }
}
