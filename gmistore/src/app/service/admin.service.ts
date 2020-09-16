import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserRegistrationsCounterModel} from "../models/UserRegistrationsCounterModel";
import {UserModel} from "../models/user-model";
import {UserIsActiveModel} from "../models/userIsActiveModel";
import {UserListDetailsModel} from "../models/UserListDetailsModel";
import {RolesInitModel} from "../models/rolesInitModel";
import {MainCategoryModel} from "../models/main-category.model";
import {NewCategoryModel} from "../models/new-category.model";
import {UserEditableDetailsByAdmin} from "../models/userEditableDetailsByAdmin";
import {OrderListModel} from "../models/order/orderListModel";

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private adminUrl = environment.apiUrl + 'api/admin';
  private lookupUrl = environment.apiUrl + 'api/lookup';

  constructor(private httpClient: HttpClient) {
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
}
