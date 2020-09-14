import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {AuthService} from "../../../service/auth-service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css']
})
export class PasswordResetComponent implements OnInit {
  resetPassForm: FormGroup;
  private formValue: any;

  constructor(private formBuilder: FormBuilder,
              private authService:AuthService,
              private router:Router,
              ) {
    this.resetPassForm = this.formBuilder.group({
      email: [null]
    });
  }

  ngOnInit(): void {
  }

  onSubmit() {
    this.formValue = this.resetPassForm.value;
    this.authService.sendResetMail(this.formValue).subscribe(
      (r) => console.log(r),
      (error) => console.log(error),
      () => this.router.navigate(['/login'])
    )
  }
}
