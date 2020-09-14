import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../../../service/auth-service";
import {ActivatedRoute} from "@angular/router";
import {PasswordResetToken} from "../../../../models/passwordResetToken";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-new-password',
  templateUrl: './new-password.component.html',
  styleUrls: ['./new-password.component.css']
})
export class NewPasswordComponent implements OnInit {
  hide: boolean = true;

  resetPasswordData = new class implements PasswordResetToken {
    password: string;
    confirmPassword: string;
    token: string;
  };

  newPasswordForm: FormGroup;

  constructor(private authService: AuthService,
              private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder) {


    let token;
    this.activatedRoute.queryParams.subscribe(params => {
      token = params['token'];
      this.resetPasswordData.token = token ;
    });

    this.newPasswordForm = this.formBuilder.group({
      password: [null],
      confirmPassword: [null],
    });
  }

  ngOnInit(): void {

  }

  onSubmit() {
    console.log(this.resetPasswordData);
    this.resetPasswordData.password = this.newPasswordForm.value.password;
    this.resetPasswordData.confirmPassword = this.newPasswordForm.value.confirmPassword;
    this.authService.confirmResetPassword(this.resetPasswordData).subscribe(
      data=>{console.log(data);
    },error => {console.log(error)},
      ()=>{
    });
  }

}
