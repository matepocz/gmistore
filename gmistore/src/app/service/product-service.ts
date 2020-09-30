import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ProductModel} from "../models/product-model";
import {environment} from "../../environments/environment";
import {ProductCategoryModel} from "../models/product-category.model";
import {PagedProductListModel} from "../models/product/paged-product-list.model";
import {ProductFilterOptions} from "../models/product/product-filter-options";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private productsUrl = environment.apiUrl + 'api/products';
  private lookupUrl = environment.apiUrl + 'api/lookup';

  constructor(private httpClient: HttpClient) {
  }

  addProduct(product: ProductModel): Observable<any> {
    return this.httpClient.post(this.productsUrl, product);
  }

  getProductBySlug(slug: string): Observable<ProductModel> {
    return this.httpClient.get<ProductModel>(this.productsUrl + '/' + slug);
  }

  getProductNamesForAutocomplete(name: string): Observable<Array<string>> {
    return this.httpClient.get<Array<string>>(this.productsUrl + '/product-names/' + name);
  }

  getProductsBySearchInput(
    input: string, pageIndex: number, pageSize: number, filterOptions?: ProductFilterOptions
  ): Observable<PagedProductListModel> {
    let params = {
      filter: filterOptions ? "true" : "false",
      query: input,
      size: pageSize.toString(),
      page: pageIndex.toString(),
    };
    return this.httpClient.post<PagedProductListModel>(
      this.productsUrl + '/search',
      filterOptions,
      {params: params});
  }

  getDiscountedProducts(
    pageIndex: number, pageSize: number, filterOptions?: ProductFilterOptions
  ): Observable<PagedProductListModel> {
    let params = {
      filter: filterOptions ? "true" : "false",
      size: pageSize.toString(),
      page: pageIndex.toString()
    }
    return this.httpClient.post<PagedProductListModel>(
      this.productsUrl + '/discounted-products',
      filterOptions,
      {params: params}
    );
  }

  getProductsByCategory(
    category: string, page: number, size: number, filterOptions?: ProductFilterOptions
  ): Observable<PagedProductListModel> {
    let params = {
      filter: filterOptions ? "true" : "false",
      size: size.toString(),
      page: page.toString()
    }
    return this.httpClient.post<PagedProductListModel>(
      this.productsUrl + '/by-category/' + category, filterOptions, {params: params});
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

  getDiscountProducts(): Observable<Array<ProductModel>> {
    return this.httpClient.get<Array<ProductModel>>(this.productsUrl + "/get-discount-product")
  }

  deleteProduct(id: number): Observable<boolean> {
    return this.httpClient.delete<boolean>(this.productsUrl + '/' + id);
  }
}
