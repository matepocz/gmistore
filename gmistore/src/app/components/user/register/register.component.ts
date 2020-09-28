import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {RegisterRequestModel} from "../../../models/register-request-model";
import {AuthService} from "../../../service/auth-service";
import {ComparePassword} from "../../../utils/password-validator";
import {Router} from "@angular/router";
import {usernameValidator} from "../../../utils/username-validator";
import {emailValidator} from "../../../utils/email-validator";
import {Subscription} from "rxjs";
import {errorHandler} from "../../../utils/error-handler";


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit, OnDestroy {
  @ViewChild('form') formValues;
  registerForm: FormGroup;
  registerRequest: RegisterRequestModel
  hidePassword: boolean = true;
  hideConfirmPassword: boolean = true;
  isSeller: boolean = false;

  authenticatedUser: boolean = false;

  registerSubscription: Subscription;

  constructor(private formBuilder: FormBuilder, private authService: AuthService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.authenticatedUser = this.authService.isAuthenticated();
    if (this.authenticatedUser) {
      this.router.navigate(['/']);
    }
    this.registerForm = this.formBuilder.group({
      firstName: [null,
        [Validators.required, Validators.minLength(3), Validators.maxLength(100),
          Validators.pattern("^[-'a-zA-ZÀ-ÖØ-öø-ſ]+$"), Validators.nullValidator]],
      lastName: [null,
        [Validators.required, Validators.minLength(3), Validators.maxLength(100),
          Validators.pattern("^[-'a-zA-ZÀ-ÖØ-öø-ſ]+$"), Validators.nullValidator]],
      username: [null,
        [Validators.required, Validators.minLength(5), Validators.maxLength(100),
          Validators.pattern("^[a-z0-9_-]{5,30}$"), Validators.nullValidator],
        [usernameValidator(this.authService)]],
      email: [null, Validators.compose(
        [Validators.required, Validators.email, Validators.maxLength(100)]),
        [emailValidator(this.authService)]],
      password: [null,
        [Validators.required, Validators.nullValidator,
          Validators.pattern("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}")]],
      confirmPassword: [null, [Validators.required]],
      seller: [false,]
    }, {validator: ComparePassword('password', 'confirmPassword')});
  }


  onSubmit() {
    console.log(this.registerForm.get('username'));
    this.registerRequest = this.registerForm.value;

    this.registerSubscription = this.authService.register(this.registerRequest).subscribe(
      (data) => {
      }, (error) => {
        errorHandler(error, this.registerForm);
      }, () => {
        this.router.navigate(['/register-success'])
      }
    );
  }

  ngOnDestroy() {
    if (this.registerSubscription) {
      this.registerSubscription.unsubscribe();
    }
  }
}
