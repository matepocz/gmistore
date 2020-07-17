import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Product} from "../product/product";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private productsUrl = environment.apiUrl + 'api/products/';

  constructor(private http: HttpClient) {
  }

  addProduct(product: Product): Observable<any> {
    return this.http.post(this.productsUrl + 'add', product);
  }

  getProduct(id: number): Observable<Product> {
    return this.http.get<Product>(this.productsUrl + 'get/' + id);
  }

  getActiveProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.productsUrl + 'all');
  }

  getProductCategories(): Observable<String[]> {
    return this.http.get<String[]>(this.productsUrl + 'get-product-categories');
  }
}
