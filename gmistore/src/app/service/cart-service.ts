import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from "../../environments/environment";
import {UserService} from "./user.service";
import {LocalStorageService} from "ngx-webstorage";
import {Product} from "../models/product";


@Injectable({providedIn: 'root'})
export class CartService {

  private cartUrl = environment.apiUrl + '/api/cart';

  localMap: Map<Product, number>;

  constructor(private httpClient: HttpClient, private userService: UserService,
              private localStorageService: LocalStorageService) {
  }

  storeLocalCart() {
    this.localStorageService.store('localCart', this.localMap);
  }

  addItem (product: Product, amount: number) {
    if (this.localMap.has(product)) {
      this.localMap.set(product, this.localMap.get(product) + amount);
    } else {
      this.localMap.set(product, amount);
    }
    this.localStorageService.clear('localCart');
    this.localStorageService.store('localCart', this.localMap);
  }

  removeItem(product: Product) {
    this.localMap.delete(product);
  }

  clearLocalCart () {
    this.localStorageService.clear('localCart');
    this.localMap.clear();
  }
}
