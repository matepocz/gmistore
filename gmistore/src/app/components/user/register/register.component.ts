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

  registerSubscription: Subscription;

  constructor(private formBuilder: FormBuilder, private authService: AuthService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      firstName: [null,
        [Validators.required, Validators.min(3), Validators.max(30), Validators.nullValidator]],
      lastName: [null,
        [Validators.required, Validators.min(3), Validators.max(15), Validators.nullValidator]],
      username: [null,
        [Validators.required, Validators.min(5), Validators.max(15), Validators.nullValidator],
        [usernameValidator(this.authService)]],
      email: [null, [Validators.required, Validators.email], [emailValidator(this.authService)]],
      password: [null,
        [Validators.required, Validators.nullValidator,
          Validators.pattern("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}")]],
      confirmPassword: [null, [Validators.required]],
      seller: [false,]
    }, {validator: ComparePassword('password', 'confirmPassword')});
  }


  onSubmit() {
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
