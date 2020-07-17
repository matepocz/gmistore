import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {RegisterPayload} from "../auth/register-payload";
import {AuthService} from "../auth/auth.service";
import {ComparePassword} from "../auth/passwordValidator";


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm:FormGroup;
  registerPayload: RegisterPayload

  constructor(private formBuilder: FormBuilder, private authService: AuthService) {
    this.registerForm = this.formBuilder.group({
      firstName: new FormControl('',[Validators.required,Validators.min(3),Validators.max(30),Validators.nullValidator]),  //todo folytatni a regex betűzéssel
      lastName: new FormControl('',[Validators.required,Validators.min(3),Validators.max(15),Validators.nullValidator]),
      username: new FormControl('',[Validators.required,Validators.min(5),Validators.max(15),Validators.nullValidator]),
      email: new FormControl('',[Validators.required,Validators.email]),
      password: new FormControl('',[Validators.required,Validators.nullValidator,Validators.pattern("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}")]),
      confirmPassword: new FormControl('',[Validators.required])
    },{validator: ComparePassword});

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
