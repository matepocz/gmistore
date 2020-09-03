import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserRegistrationsCounterModel} from "../models/UserRegistrationsCounterModel";
import {UserModel} from "../models/user-model";

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private adminUrl = environment.apiUrl + 'admin/';

  constructor(private httpClient: HttpClient) {
  }

  getUserRegistrationsCount(): Observable<UserRegistrationsCounterModel> {
    return this.httpClient.get<UserRegistrationsCounterModel>(this.adminUrl + "registered");
  }

  getAccount(id:number): Observable<UserModel> {
    return this.httpClient.get<UserModel>(this.adminUrl + 'user/' + id);
  }

  getUserList(): Observable<Array<UserModel>> {
    return this.httpClient.get<Array<UserModel>>(this.adminUrl + 'users');
  }

}
