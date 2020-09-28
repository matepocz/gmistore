import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from "../../environments/environment";
import {UserService} from "./user.service";
import {Observable} from "rxjs";
import {CartModel} from "../models/cart-model";
import {ShippingMethodModel} from "../models/shipping-method-model";

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private cartUrl = environment.apiUrl + 'api/carts';

  constructor(private httpClient: HttpClient, private userService: UserService) {
  }

  getNumberOfItemsInCart(): Observable<number> {
    return this.httpClient.get<number>(this.cartUrl + '/items-in-cart');
  }

  getCart(): Observable<CartModel> {
    return this.httpClient.get<CartModel>(this.cartUrl);
  }

  addProduct(id: number): Observable<boolean> {
    let params = new HttpParams();
    params = params.append('id', String(id));
    params = params.append('count', String(1));
    return this.httpClient.put<boolean>(
      this.cartUrl + '/add-item',
      {},
      {params},
    );
  }

  refreshProductCount(id: number, count: number): Observable<any> {
    let params = new HttpParams();
    params = params.append('id', String(id));
    params = params.append('count', String(count));
    return this.httpClient.put(
      this.cartUrl + '/refresh-product-count',
      {},
      {params});
  }

  removeProduct(id: number): Observable<any> {
    let params = new HttpParams();
    params = params.append('id', String(id));
    return this.httpClient.delete(this.cartUrl + '/remove-product', {params})
  }

  getShippingData(): Observable<Array<ShippingMethodModel>> {
    return this.httpClient.get<Array<ShippingMethodModel>>(this.cartUrl + '/shipping-data');
  }

  updateShippingMethod(value: string) {
    let params = new HttpParams();
    params = params.append('method', String(value));
    return this.httpClient.put(this.cartUrl + '/update-shipping-method',
      {},
      {params}
    );
  }

  canCheckout(): Observable<boolean> {
    return this.httpClient.get<boolean>(this.cartUrl + '/can-checkout');
  }
}
