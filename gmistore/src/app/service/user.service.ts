import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {UserModel} from "../models/user/user-model";
import {UserEditableDetailsByUser} from "../models/user/userEditableDetailsByUser";
import {ProductModel} from "../models/product-model";


@Injectable({
  providedIn: 'root'
})
export class UserService {
  apiUrl = environment.apiUrl + 'api/';
  userUrl = environment.apiUrl + 'api/user';

  constructor(private httpClient: HttpClient) {
  }

  getUser(): Observable<UserModel> {
    return this.httpClient.get<UserModel>(this.apiUrl + 'user/my-account');
  }

  getLoggedInUsers(): Observable<any> {
    return this.httpClient.get(this.apiUrl + 'user/loggedUsers');
  }

  updateUser(data: UserEditableDetailsByUser): Observable<any> {
    return this.httpClient.put(this.apiUrl + 'user/edit', data);
  }

  getCountOfFavoriteProducts(): Observable<number> {
    return this.httpClient.get<number>(this.userUrl + '/count-of-favorite-products');
  }

  addProductToFavorites(id: number): Observable<boolean> {
    return this.httpClient.post<boolean>(this.userUrl + '/favorite-products/' + id, {});
  }

  getFavoriteProducts(): Observable<Array<ProductModel>> {
    return this.httpClient.get<Array<ProductModel>>(this.userUrl + '/favorite-products');
  }

  removeProductFromFavorites(id: number): Observable<boolean> {
    return this.httpClient.delete<boolean>(this.userUrl + '/favorite-products/' + id);
  }
}

