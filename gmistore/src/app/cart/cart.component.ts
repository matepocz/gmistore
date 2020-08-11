import {Component, OnDestroy, OnInit} from '@angular/core';
import {CartService} from "../service/cart-service";
import {CartModel} from "../models/cart-model";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit, OnDestroy {

  loading = false;
  cart: CartModel;
  cartSubscription: Subscription;
  refreshSubscription: Subscription;
  removeProductSubscription: Subscription;

  constructor(private cartService: CartService) {}

  ngOnInit(): void {
    this.loading = true;
    this.cartSubscription = this.cartService.getCart().subscribe(
      (data) => {
        this.cart = data;
      }, (error) => {
        console.log(error);
      }, () => this.loading = false
    )
  }

  refreshProductCount(id: number, count: string) {
    this.loading = true;
    this.refreshSubscription = this.cartService.refreshProductCount(id, +count).subscribe(
      (response) => {
        this.ngOnInit();
      }, (error)=> {
        console.log(error);
      }, () => this.loading = false
    )
  }

  removeProduct(id: number) {
    this.loading = true;
    this.removeProductSubscription = this.cartService.removeProduct(id).subscribe(
      (response) => {
        this.ngOnInit();
      }, (error) => {
        console.log(error)
      }, () => this.loading = false
    )
  }

  ngOnDestroy() {
    this.cartSubscription.unsubscribe();
    if (this.refreshSubscription){
      this.refreshSubscription.unsubscribe();
    }
    if (this.removeProductSubscription) {
      this.removeProductSubscription.unsubscribe();
    }
  }
}
