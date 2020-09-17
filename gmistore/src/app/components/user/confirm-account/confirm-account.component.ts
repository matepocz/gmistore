import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthService} from "../../../service/auth-service";

@Component({
  selector: 'app-confirm-account',
  templateUrl: './confirm-account.component.html',
  styleUrls: ['../register/register.component.css']
})
export class ConfirmAccountComponent implements OnInit {

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  ngOnInit(): void {
    this.authService.confirmAccount().subscribe(data => {
    }, error => {
    }, () => {

    });
  }


}
