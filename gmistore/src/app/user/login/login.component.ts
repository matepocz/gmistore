import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {LoginRequestModel} from "../../models/login-request-model";
import {AuthService} from "../../service/auth-service";
import {Router} from "@angular/router";
import {Subscription} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['../register/register.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {

  loginForm: FormGroup;
  loginPayload: LoginRequestModel;
  loginSubscription: Subscription;
  hide: boolean = true;

  constructor(private authService: AuthService, private router: Router, private formBuilder: FormBuilder) {
    this.loginForm = this.formBuilder.group({
      username: [null],
      password: [null]
    });
  }

  ngOnInit() {
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/'])
    }
  }

  onSubmit() {
    this.loginPayload = this.loginForm.value;
    this.authService.login(this.loginPayload).subscribe(
      (data) => {
        if (data) {
          this.router.navigate(['/product-list'])
        } else {
          this.loginForm.get('username').setErrors({badCredentials: true})
        }
      }, (error: HttpErrorResponse) => {
        this.loginForm.get('username').setErrors({badCredentials: true})
      });
  }

  ngOnDestroy() {
    if (this.loginSubscription) {
      this.loginSubscription.unsubscribe();
    }
  }
}
