import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {CustomerDetailsModel} from "../models/customer-details.model";
import {OrderRequestModel} from "../models/order-request.model";

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private ordersUrl = environment.apiUrl + 'api/orders';

  constructor(private httpClient: HttpClient) {
  }

  getCustomerDetails(): Observable<CustomerDetailsModel> {
    return this.httpClient.get<CustomerDetailsModel>(this.ordersUrl + '/customer-details');
  }

  createOrder(data: OrderRequestModel): Observable<any> {
    return this.httpClient.post(this.ordersUrl, data);
  }
}
