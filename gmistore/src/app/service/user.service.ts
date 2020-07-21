import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {User} from "../models/user";


@Injectable({
  providedIn: 'root'
})
export class UserService {
  apiUrl = environment.apiUrl + 'api/';

  constructor(private httpClient: HttpClient) {
  }

  getUser(): Observable<User> {
    return this.httpClient.get<User>(this.apiUrl + 'user/my-account');
  }
}

