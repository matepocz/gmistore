import {EventEmitter, Injectable, Output} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserRegistrationsCounterModel} from "../models/user/UserRegistrationsCounterModel";
import {UserModel} from "../models/user/user-model";
import {UserIsActiveModel} from "../models/user/userIsActiveModel";
import {UserListDetailsModel} from "../models/user/UserListDetailsModel";
import {RolesInitModel} from "../models/rolesInitModel";
import {MainCategoryModel} from "../models/main-category.model";
import {NewCategoryModel} from "../models/new-category.model";
import {UserEditableDetailsByAdmin} from "../models/user/userEditableDetailsByAdmin";
import {OrderListModel} from "../models/order/orderListModel";
import {UserRegistrationStartEndDateModel} from "../models/user/UserRegistrationStartEndDateModel";
import {LiveDataSubjectService} from "./live-data-subject.service";
import {ProductTableModel} from "../models/product/productTableModel";
import {IncomeByDateModel} from "../models/order/IncomeByDateModel";
import {DashBoardBasicModel} from "../models/DashBoardBasicModel";

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private adminUrl = environment.apiUrl + 'api/admin';
  private lookupUrl = environment.apiUrl + 'api/lookup';
  evs: EventSource;
  @Output() emitData: EventEmitter<any> = new EventEmitter<any>();

  constructor(private httpClient: HttpClient,
              private subj: LiveDataSubjectService) {
  }

  getExchangeData() {
    let subject = this.subj;
    if (this.evs == null) {

      if (typeof (EventSource) !== undefined) {
        this.evs = new EventSource(this.adminUrl + '/sse');
        this.evs.onopen = function (e) {
          console.log('Opening connection.Ready State is' + this.readyState);
        }
        this.evs.onmessage = function (e) {
          console.log('Message Received.Ready State is ' + this.readyState);
          console.log(JSON.parse(e.data))

          subject.updatedDataSelection(JSON.parse(e.data));
        }
        // this.evs.addEventListener("timestamp", function (e) {
        //   console.log("Timestamp event Received.Ready State is " + this.readyState);
        //   subject.updatedDataSelection(e["data"]);
        // })
        this.evs.onerror = function (e) {
          console.log(e);
          if (this.readyState == 0) {
            console.log('Stopping…');
            this.close();
          }
        }
      }
    }
  }

  stopExchangeUpdates() {
    this.evs.close();
  }

  returnAsObservable() {
    return this.subj.asObservable();
  }

  getUserRegistrationsCount(): Observable<UserRegistrationsCounterModel> {
    return this.httpClient.get<UserRegistrationsCounterModel>(this.adminUrl + "/registered");
  }

  getAccount(id: number): Observable<UserModel> {
    return this.httpClient.get<UserModel>(this.adminUrl + '/users/' + id);
  }

  getUserList(): Observable<Array<UserListDetailsModel>> {
    return this.httpClient.get<Array<UserListDetailsModel>>(this.adminUrl + '/users');
  }

  setUserActivity(userIsActiveData: UserIsActiveModel) {
    console.log(userIsActiveData)
    return this.httpClient.put(this.adminUrl + '/users/active', userIsActiveData);
  }

  getInitRoles(): Observable<RolesInitModel[]> {
    return this.httpClient.get<Array<RolesInitModel>>(this.adminUrl + '/users/roles');
  }

  getProductCategories(): Observable<Array<MainCategoryModel>> {
    return this.httpClient.get<Array<MainCategoryModel>>(this.lookupUrl + '/categories');
  }

  createNewProductCategory(data: NewCategoryModel): Observable<boolean> {
    return this.httpClient.post<boolean>(this.lookupUrl + '/new-category', data);
  }

  setCategoryInactive(key: string): Observable<boolean> {
    return this.httpClient.delete<boolean>(this.lookupUrl + '/category/' + key);
  }

  updateUser(data: UserEditableDetailsByAdmin): Observable<any> {
    return this.httpClient.put(this.adminUrl + '/users', data);
  }

  getOrders(): Observable<Array<OrderListModel>> {
    return this.httpClient.get<Array<OrderListModel>>(this.adminUrl + '/orders');
  }

  getStatusOptions(): Observable<Array<string>> {
    return this.httpClient.get<Array<string>>(this.adminUrl + '/orders/orderOptions');
  }

  getUserRegistrationsCountByDate(dates: UserRegistrationStartEndDateModel): Observable<UserRegistrationsCounterModel> {
    console.log(dates)
    return this.httpClient.get<UserRegistrationsCounterModel>(this.adminUrl + '/registered' +
      '/?criteria=' + encodeURIComponent(JSON.stringify(dates)));
  }

  getIncomePerOrder(dates: UserRegistrationStartEndDateModel): Observable<IncomeByDateModel> {
    console.log(dates)
    return this.httpClient.get<IncomeByDateModel>(this.adminUrl + '/income' +
      '/?criteria=' + encodeURIComponent(JSON.stringify(dates)));
  }

  fetchProductsTableData(): Observable<ProductTableModel[]> {
    return this.httpClient.get<Array<ProductTableModel>>(this.adminUrl + '/products');
  }

  fetchDashboardData(): Observable<DashBoardBasicModel> {
    return this.httpClient.get<DashBoardBasicModel>(this.adminUrl + '/dashboard');
  }
}
