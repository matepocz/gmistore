import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from "../../environments/environment";
import {UserService} from "./user.service";
import {Observable} from "rxjs";
import {CartModel} from "../models/cart-model";

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private cartUrl = environment.apiUrl + 'api/carts';

  constructor(private httpClient: HttpClient, private userService: UserService) {
  }

  getCart(): Observable<CartModel> {
    return this.httpClient.get<CartModel>(this.cartUrl, {withCredentials: true});
  }

  addProduct(id: number): Observable<any> {
    let params = new HttpParams();
    params = params.append('id', String(id));
    params = params.append('count', String(1));
    return this.httpClient.put(
      this.cartUrl + '/add-item',
      {},
      {withCredentials: true, params},
      );
  }

  refreshProductCount(id: number, count: number): Observable<any> {
    let params = new HttpParams();
    params = params.append('id', String(id));
    params = params.append('count', String(count));
    return this.httpClient.put(
      this.cartUrl + '/refresh-product-count',
      {},
      {withCredentials: true, params});
  }

  removeProduct(id: number): Observable<any> {
    let params = new HttpParams();
    params = params.append('id', String(id));
    return this.httpClient.delete(this.cartUrl + '/remove-product', {withCredentials: true, params})
  }
}
