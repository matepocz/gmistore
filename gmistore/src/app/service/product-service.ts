import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ProductModel} from "../models/product-model";
import {environment} from "../../environments/environment";
import {ProductCategoryModel} from "../models/product-category.model";
import {PagedProductListModel} from "../models/product/paged-product-list.model";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private productsUrl = environment.apiUrl + 'api/products';
  private imageUploadUrl = environment.apiUrl + 'api/images/upload';
  private lookupUrl = environment.apiUrl + 'api/lookup';

  constructor(private httpClient: HttpClient) {
  }

  addProduct(product: ProductModel): Observable<any> {
    return this.httpClient.post(this.productsUrl, product);
  }

  getProductBySlug(slug: string): Observable<ProductModel> {
    return this.httpClient.get<ProductModel>(this.productsUrl + '/' + slug);
  }

  getActiveProducts(): Observable<ProductModel[]> {
    return this.httpClient.get<ProductModel[]>(this.productsUrl);
  }

  getProductsByCategory(category: string, page: number, size: number): Observable<PagedProductListModel> {
    let params = {
      size: size.toString(),
      page: page.toString()
    }
    return this.httpClient.get<PagedProductListModel>(
      this.productsUrl + '/by-category/' + category,
      {params: params});
  }

  getMainProductCategories(): Observable<Array<ProductCategoryModel>> {
    return this.httpClient.get<Array<ProductCategoryModel>>(this.lookupUrl + '/main-product-categories');
  }

  getSubProductCategories(id: number): Observable<Array<ProductCategoryModel>> {
    return this.httpClient.get<Array<ProductCategoryModel>>(this.lookupUrl + '/sub-product-categories/' + id);
  }

  updateProduct(product: ProductModel, slug: string): Observable<any> {
    return this.httpClient.put(this.productsUrl + '/' + slug, product);
  }

  public async uploadImage(image: File) {
    const uploadData = new FormData();
    uploadData.append('picture', image);

    return await this.httpClient.post(this.imageUploadUrl, uploadData).toPromise();
  }

  getDiscountProductsProductURL(): Observable<any> {
    return this.httpClient.get(this.productsUrl + "/get-discount-pictures")
  }
}
