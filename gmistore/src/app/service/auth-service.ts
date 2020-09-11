import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {RegisterRequestModel} from "../models/register-request-model";
import {BehaviorSubject, Observable} from "rxjs";
import {LoginRequestModel} from "../models/login-request-model";
import {LoginResponseModel} from "../models/login-response.model";
import {delay, map} from "rxjs/operators";
import {LocalStorageService} from "ngx-webstorage";
import {ActivatedRoute, Router} from "@angular/router";
import {environment} from "../../environments/environment";
import {RoleModel} from "../models/role.model";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authUrl = environment.apiUrl + 'api/auth/'

  private currentUserRoles: BehaviorSubject<Array<RoleModel>>;
  public userRoles: Observable<Array<RoleModel>>;

  public isAdmin: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public isSeller: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor(private httpClient: HttpClient, private localStorageService: LocalStorageService,
              private _router: Router, private activatedRoute: ActivatedRoute) {
    this.fetchCurrentUserRoles();
  }

  register(registerPayload: RegisterRequestModel): Observable<any> {
    return this.httpClient.post(this.authUrl + "register", registerPayload)
  }

  confirmAccount() {
    let confirmAccountToken = '';
    this.activatedRoute.queryParams.subscribe(params => {
      confirmAccountToken = params['token'];
    });
    return this.httpClient.get(this.authUrl + "confirm-account/" + confirmAccountToken)
  }

  login(loginPayload: LoginRequestModel): Observable<boolean> {
    return this.httpClient.put<LoginResponseModel>(this.authUrl + 'login', loginPayload)
      .pipe(map(response => {
          this.fetchCurrentUserRoles();
          if (response && response.authenticationToken) {
            this.localStorageService.store('authenticationToken', response.authenticationToken);
            this.localStorageService.store('username', response.username);
          }
          return true;
        })
      );
  }

  private async fetchCurrentUserRoles() {
    await this.getUserRoles().then(
      (response) => {
        this.currentUserRoles = new BehaviorSubject<Array<RoleModel>>(response);
        this.userRoles = this.currentUserRoles.asObservable();

        if (this.currentUserRoles.value.indexOf(RoleModel.ROLE_ADMIN) !== -1) {
          this.isAdmin.next(true);
        }
        if (this.currentUserRoles.value.indexOf(RoleModel.ROLE_SELLER) !== -1) {
          this.isSeller.next(true);
        }
      },
    );
  }

  async getUserRoles(): Promise<Array<RoleModel>> {
    return await this.httpClient.get<Array<RoleModel>>(this.authUrl + 'user-roles').toPromise();
  }

  isAuthenticated(): boolean {
    return this.localStorageService.retrieve('username') != null;
  }

  public get currentUsername(): string {
    if (this.localStorageService.retrieve('username')) {
      return this.localStorageService.retrieve('username');
    }
    return null;
  }

  checkIfUsernameTaken(username: string): Observable<boolean> {
    return this.httpClient.get<boolean>(
      this.authUrl + 'check-username-taken/' + username).pipe(delay(1000));
  }

  checkIfEmailTaken(email: string): Observable<boolean> {
    return this.httpClient.get<boolean>(
      this.authUrl + 'check-email-taken/' + email).pipe(delay(1000));
  }

  logout(): Observable<any> {
    this.localStorageService.clear('authenticationToken');
    this.localStorageService.clear('username');
    this.isAdmin.next(false);
    this.isSeller.next(false);
    this.currentUserRoles.next(new Array<RoleModel>());
    return this.httpClient.post(environment.apiUrl + 'api/logout', {});
  }
}
