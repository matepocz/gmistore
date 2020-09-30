import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {PagedSellerProductListModel} from "../../models/product/PagedSellerProductListModel";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class SellerService {
  private productsUrl = environment.apiUrl + 'api/products';

  constructor(private http: HttpClient) {
  }

  getOwnProducts(
    page: number, size: number,


  ): Observable<PagedSellerProductListModel> {
  let params = {
    size: size.toString(),
    page: page.toString(),
  }
    return this.http.get<PagedSellerProductListModel>(this.productsUrl+'/added-by-user',{params: params});
  }
}
