import {Component, OnDestroy, OnInit} from '@angular/core';
import {CartService} from "../service/cart-service";
import {CartModel} from "../models/cart-model";
import {Subscription} from "rxjs";
import {ShippingMethodModel} from "../models/shipping-method-model";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit, OnDestroy {

  loading = false;
  cart: CartModel;
  shippingData: ShippingMethodModel[];
  currentShipping: ShippingMethodModel = null;

  cartSubscription: Subscription;
  shippingDataSubscription: Subscription;
  refreshSubscription: Subscription;
  removeProductSubscription: Subscription;
  refreshShippingSubscription: Subscription;

  constructor(private cartService: CartService) {
  }

  ngOnInit(): void {
    this.loading = true;
    this.cartSubscription = this.cartService.getCart().subscribe(
      (data) => {
        this.cart = data;
        this.currentShipping = data.shippingMethod;
        console.log(data);
      }, (error) => {
        console.log(error);
      }, () => {
        this.loading = false;
      }
    );

    this.shippingDataSubscription = this.cartService.getShippingData().subscribe(
      (data) => {
        this.shippingData = data;
      }, (error) => console.log(error)
    )
  }

  refreshProductCount(id: number, count: string) {
    console.log(id, count)
    this.loading = true;
    this.refreshSubscription = this.cartService.refreshProductCount(id, +count).subscribe(
      (response) => {
        this.ngOnInit();
      }, (error) => {
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

  refreshSippingPrice() {
    this.loading = true;
    this.refreshShippingSubscription = this.cartService.updateShippingMethod(this.currentShipping.method).subscribe(
      (response) => {
      }, error => console.log(error),
      () => {
        this.ngOnInit();
      }
    )
  }

  ngOnDestroy() {
    this.cartSubscription.unsubscribe();
    this.shippingDataSubscription.unsubscribe();
    if (this.refreshSubscription) {
      this.refreshSubscription.unsubscribe();
    }
    if (this.removeProductSubscription) {
      this.removeProductSubscription.unsubscribe();
    }
    if (this.refreshShippingSubscription) {
      this.refreshShippingSubscription.unsubscribe();
    }
  }
}
