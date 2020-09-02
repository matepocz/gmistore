import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {UserModel} from "../models/user-model";


@Injectable({
  providedIn: 'root'
})
export class UserService {
  apiUrl = environment.apiUrl + 'api/';

  constructor(private httpClient: HttpClient) {
  }

  getUser(): Observable<UserModel> {
    return this.httpClient.get<UserModel>(this.apiUrl + 'user/my-account');
  }

  getLoggedInUsers(): Observable<any> {
    return this.httpClient.get(this.apiUrl + 'user/loggedUsers');
  }

  storeLocalCart() {

  }
}

