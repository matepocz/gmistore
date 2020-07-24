import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Product} from "../models/product";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private productsUrl = environment.apiUrl + 'api/products/';
  private imageUploadUrl = environment.apiUrl + 'api/images/upload';

  constructor(private httpClient: HttpClient) {
  }

  addProduct(product: Product): Observable<any> {
    return this.httpClient.post(this.productsUrl + 'add', product);
  }

  getProductBySlug(slug: string): Observable<Product> {
    return this.httpClient.get<Product>(this.productsUrl + 'get-by-slug/' + slug);
  }

  getActiveProducts(): Observable<Product[]> {
    return this.httpClient.get<Product[]>(this.productsUrl + 'all');
  }

  getProductCategories(): Observable<String[]> {
    return this.httpClient.get<String[]>(this.productsUrl + 'get-product-categories');
  }

  public async uploadImage(image: File) {
    const uploadData = new FormData();
    uploadData.append('picture', image);

    return await this.httpClient.post(this.imageUploadUrl, uploadData).toPromise();
  }
}
