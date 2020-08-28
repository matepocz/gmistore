import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {RegisterRequestModel} from "../models/register-request-model";
import {Observable} from "rxjs";
import {LoginRequestModel} from "../models/login-request-model";
import {JwtAuthResponse} from "../utils/jwt-auth-response";
import {delay, map} from "rxjs/operators";
import {LocalStorageService} from "ngx-webstorage";
import {ActivatedRoute, Router} from "@angular/router";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authUrl = environment.apiUrl + 'api/auth/'
  private token: string;

  constructor(private httpClient: HttpClient, private localStorageService: LocalStorageService,
              private _router: Router, private activatedRoute: ActivatedRoute) {
    this.activatedRoute.queryParams.subscribe(params => {
      this.token = params['token'];
    })
  }

  register(registerPayload: RegisterRequestModel): Observable<any> {
    return this.httpClient.post(this.authUrl + "register", registerPayload)
  }

  confirmAccount() {
    return this.httpClient.get(this.authUrl + "confirm-account/" + this.token)
  }

  login(loginPayload: LoginRequestModel): Observable<boolean> {
    return this.httpClient.put<JwtAuthResponse>(this.authUrl + 'login', loginPayload).pipe(map(data => {
      this.localStorageService.store('authenticationToken', data.authenticationToken);
      this.localStorageService.store('username', data.username);
      return true;
    }));
  }

  isAuthenticated(): boolean {
    return this.localStorageService.retrieve('username') != null;
  }

  checkIfUsernameTaken(username: string): Observable<boolean> {
    return this.httpClient.get<boolean>(
      this.authUrl + 'check-username-taken/' + username).pipe(delay(1000));
  }

  checkIfEmailTaken(email: string): Observable<boolean> {
    return this.httpClient.get<boolean>(
      this.authUrl + 'check-email-taken/' + email).pipe(delay(1000));
  }

  logout(): Observable<any>{
    this.localStorageService.clear('authenticationToken');
    this.localStorageService.clear('username');
    return this.httpClient.post(environment.apiUrl + 'logout', {});
  }
}
