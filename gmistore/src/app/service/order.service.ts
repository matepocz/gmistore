import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {CustomerDetailsModel} from "../models/customer-details.model";
import {OrderRequestModel} from "../models/order/order-request.model";
import {PaymentMethodDetailsModel} from "../models/payment-method-details.model";
import {ProductOrderedListModel} from "../models/product/productOrderedListModel";
import {OrderDetails} from "../models/order/orderDetails";
import {AddressModel} from "../models/address-model";
import {OrderStatusOptionsModel} from "../models/order/orderStatusOptionsModel";

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private ordersUrl = environment.apiUrl + 'api/orders';
  private lookupUrl = environment.apiUrl + 'api/lookup';

  constructor(private httpClient: HttpClient) {
  }

  getCustomerDetails(): Observable<CustomerDetailsModel> {
    return this.httpClient.get<CustomerDetailsModel>(this.ordersUrl + '/customer-details');
  }

  createOrder(data: OrderRequestModel): Observable<any> {
    return this.httpClient.post(this.ordersUrl, data);
  }

  getPaymentMethods(): Observable<Array<PaymentMethodDetailsModel>> {
    return this.httpClient.get<Array<PaymentMethodDetailsModel>>(this.lookupUrl + '/payment-methods');
  }

  getOrderItems() {
    return this.httpClient.get<Array<ProductOrderedListModel>>(this.ordersUrl + '/items');
  }

  getStatusOptions(): Observable<Array<OrderStatusOptionsModel>> {
    return this.httpClient.get<Array<OrderStatusOptionsModel>>(this.ordersUrl + '/statusOptions');
  }

  fetchOrderDetails(id: string): Observable<OrderDetails> {
    console.log(id)
    return this.httpClient.get<OrderDetails>(this.ordersUrl + '/' + id);
  }

  updateInvoiceAddress(id:string, data: AddressModel) {
    return this.httpClient.put(this.ordersUrl + "/address/invoice" + id, data);
  }
  updateDeliveryAddress(id:string, data: AddressModel) {
    return this.httpClient.put(this.ordersUrl + "/address/delivery/" + id, data);
  }

  updateOrderStatus(id:string, status: string) {
    console.log(status)
    return this.httpClient.put(this.ordersUrl + "/status/" + id, status);
  }
}
