import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Product} from "../product/product";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private baseUrl = 'http://localhost:8080/api/products/';

  constructor(private http: HttpClient) { }

  getProduct(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.baseUrl}get/${id}`);
  }
}
