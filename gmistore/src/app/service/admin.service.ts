import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserRegistrationsCounterModel} from "../models/UserRegistrationsCounterModel";

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
}
