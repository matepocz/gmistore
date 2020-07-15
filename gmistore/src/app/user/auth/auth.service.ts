import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {RegisterPayload} from "./register-payload";
import {Observable} from "rxjs";
import {LoginPayload} from "./login-payload";
import {JwtAuthResponse} from "./jwt-auth-response";
import {map} from "rxjs/operators";
import {LocalStorageService} from "ngx-webstorage";
import {Router} from "@angular/router";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authUrl = environment.apiUrl + 'auth/'

  constructor(private httpClient: HttpClient, private localStorageService: LocalStorageService, private _router: Router) {
  }

  register(registerPayload: RegisterPayload): Observable<any> {
    return this.httpClient.post(this.authUrl + "register", registerPayload)
  }

  login(loginPayload: LoginPayload): Observable<boolean> {
    return this.httpClient.put<JwtAuthResponse>(this.authUrl + 'login', loginPayload).pipe(map(data => {
      this.localStorageService.store('authenticationToken', data.authenticationToken);
      this.localStorageService.store('username', data.username);
      return true;
    }));
  }

  isAuthenticated(): boolean {
    return this.localStorageService.retrieve('username') != null;
  }


  logout(loginPayload: LoginPayload) {
    this.localStorageService.clear('authenticationToken');
    this.localStorageService.clear('username');
    this._router.navigate(['/home'])
  }
}
