import {Component, OnDestroy, OnInit} from '@angular/core';
import {CartService} from "../../service/cart-service";
import {CartModel} from "../../models/cart-model";
import {Subscription} from "rxjs";
import {ShippingMethodModel} from "../../models/shipping-method-model";
import {Title} from "@angular/platform-browser";
import {SideNavComponent} from "../side-nav/side-nav.component";
import {Router} from "@angular/router";

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
  checkoutSub: Subscription;

  constructor(private cartService: CartService, private titleService: Title,
              private sideNavComponent: SideNavComponent, private router: Router) {
  }

  ngOnInit(): void {
    this.titleService.setTitle("Kosár - GMI Store")
    this.loading = true;
    this.fetchCartData();

    this.shippingDataSubscription = this.cartService.getShippingData().subscribe(
      (data) => {
        this.shippingData = data;
      }, (error) => console.log(error)
    )
  }

  private fetchCartData() {
    this.cartSubscription = this.cartService.getCart().subscribe(
      (data) => {
        this.cart = data;
        this.currentShipping = data.shippingMethod;
      }, (error) => {
        console.log(error);
        this.loading = false;
      }, () => {
        this.loading = false;
      }
    );
  }

  refreshProductCount(id: number, count: string) {
    console.log(id, count)
    this.loading = true;
    this.refreshSubscription = this.cartService.refreshProductCount(id, +count).subscribe(
      (response) => {
        this.loading = false;
        this.fetchCartData();
        this.sideNavComponent.updateItemsInCart(0);
      }, (error) => {
        console.log(error);
      }, () => this.loading = false
    )
  }

  removeProduct(id: number) {
    this.loading = true;
    this.removeProductSubscription = this.cartService.removeProduct(id).subscribe(
      (response) => {
        this.sideNavComponent.updateItemsInCart(0);
        this.fetchCartData();
      }, (error) => {
        this.loading = false;
        console.log(error);
      }, () => this.loading = false
    )
  }

  refreshSippingPrice() {
    this.loading = true;
    this.refreshShippingSubscription = this.cartService.updateShippingMethod(this.currentShipping.method).subscribe(
      (response) => {
      }, error => {
        console.log(error);
        this.fetchCartData();
        this.loading = false;
      },
      () => {
        this.loading = false;
      }
    )
  }

  checkout() {
    this.loading = true;
    this.checkoutSub = this.cartService.canCheckout().subscribe(
      (response: boolean) => {
        console.log(response);
        this.loading = false;
        if (response) {
          this.router.navigate(['/checkout']);
        }
      }, (error) => {
        console.log(error);
        this.loading = false;
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
    if (this.checkoutSub) {
      this.checkoutSub.unsubscribe();
    }
  }
}
