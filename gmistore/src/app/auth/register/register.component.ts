import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {RegisterPayload} from "../register-payload";
import {AuthService} from "../auth.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerForm: FormGroup;
  registerPayload: RegisterPayload

  constructor(private formBuilder: FormBuilder, private authService: AuthService) {
    this.registerForm = this.formBuilder.group({
      firstName:'',
      lastName:'',
      username: '',
      email: '',
      password: '',
      confirmPassword: ''
    });
    this.registerPayload = {
      firstName: '',
      lastName: '',
      username: '',
      email: '',
      password: '',
      confirmPassword: ''
    };

  }

  ngOnInit(): void {
  }

  onSubmit() {
    this.registerPayload.firstName = this.registerForm.get('firstName').value;
    this.registerPayload.lastName = this.registerForm.get('lastName').value;
    this.registerPayload.username = this.registerForm.get('username').value;
    this.registerPayload.email = this.registerForm.get('email').value;
    this.registerPayload.password = this.registerForm.get('password').value;
    this.registerPayload.confirmPassword = this.registerForm.get('confirmPassword').value;

    this.authService.register(this.registerPayload).subscribe(data => {
      console.log('register success');
    }, error => {
      console.log('register failed');
    });
  }

}
