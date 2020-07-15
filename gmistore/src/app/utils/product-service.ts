import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Product} from "../product/product";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private productsUrl = environment.apiUrl + 'products/';

  constructor(private http: HttpClient) {
  }

  getProduct(id: number): Observable<Product> {
    return this.http.get<Product>(this.productsUrl + 'get/' + id);
  }
}
