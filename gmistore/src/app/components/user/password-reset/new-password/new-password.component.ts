import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthService} from "../../../../service/auth-service";
import {ActivatedRoute, Router} from "@angular/router";
import {PasswordResetToken} from "../../../../models/passwordResetToken";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ComparePassword} from "../../../../utils/password-validator";
import {errorHandler} from "../../../../utils/error-handler";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-new-password',
  templateUrl: './new-password.component.html',
  styleUrls: ['./new-password.component.css']
})
export class NewPasswordComponent implements OnDestroy{

  hide: boolean = true;

  resetPasswordData = new class implements PasswordResetToken {
    password: string;
    confirmPassword: string;
    token: string;
  };

  newPasswordForm: FormGroup;
  private resetPasswordSubscription: Subscription;

  constructor(private authService: AuthService,
              private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder,
              private router: Router) {

    let token;
    this.activatedRoute.queryParams.subscribe(params => {
      token = params['token'];
      this.resetPasswordData.token = token;
    });

    this.newPasswordForm = this.formBuilder.group({
        password: [null,
          [Validators.required, Validators.pattern("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}")]],
        confirmPassword: [null, [Validators.required]],
      }, {validator: ComparePassword('password', 'confirmPassword')}
    );
  }

  onSubmit() {
    console.log(this.resetPasswordData);
    this.resetPasswordData.password = this.newPasswordForm.value.password;
    this.resetPasswordData.confirmPassword = this.newPasswordForm.value.confirmPassword;
    this.resetPasswordSubscription = this.authService.confirmResetPassword(this.resetPasswordData).subscribe(
      data => console.log(data),
      error => errorHandler(error, this.newPasswordForm),
      () => this.router.navigate(['/login'])
    );
  }

  ngOnDestroy() {
    if (this.resetPasswordSubscription) {
      this.resetPasswordSubscription.unsubscribe();
    }
  }
}
