import {Component, OnDestroy, OnInit} from '@angular/core';
import {CartService} from "../../service/cart-service";
import {CartModel} from "../../models/cart-model";
import {Subscription} from "rxjs";
import {ShippingMethodModel} from "../../models/shipping-method-model";
import {Title} from "@angular/platform-browser";
import {SideNavComponent} from "../side-nav/side-nav.component";
import {Router} from "@angular/router";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";

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

  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'bottom';

  cartSubscription: Subscription;
  shippingDataSubscription: Subscription;
  refreshSubscription: Subscription;
  removeProductSubscription: Subscription;
  refreshShippingSubscription: Subscription;
  checkoutSub: Subscription;

  constructor(private cartService: CartService, private titleService: Title,
              private sideNavComponent: SideNavComponent, private router: Router,
              private snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.titleService.setTitle("Kosár - GMI Store")
    this.loading = true;
    this.fetchCartData();

    this.shippingDataSubscription = this.cartService.getShippingData().subscribe(
      (data: Array<ShippingMethodModel>) => {
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
      (response: boolean) => {
        this.fetchCartData();
        this.openSnackBar("Mennyiség frissítve!");
        this.sideNavComponent.updateItemsInCart(0);
        this.loading = false;
      }, (error) => {
        console.log(error);
        this.openSnackBar("Valami hiba történt!");
      }, () => this.loading = false
    )
  }

  removeProduct(id: number) {
    this.loading = true;
    this.removeProductSubscription = this.cartService.removeProduct(id).subscribe(
      (response: boolean) => {
        if (response) {
          this.openSnackBar("Termék törölve!");
        }
        this.fetchCartData();
        this.sideNavComponent.updateItemsInCart(0);
      }, (error) => {
        this.loading = false;
        console.log(error);
        this.openSnackBar("Valami hiba történt!");
      }, () => this.loading = false
    )
  }

  updateShippingMethod() {
    this.loading = true;
    this.refreshShippingSubscription = this.cartService.updateShippingMethod(this.currentShipping.method).subscribe(
      () => {
        this.fetchCartData();
      }, error => {
        console.log(error);
        this.loading = false;
        this.openSnackBar("Valami hiba történt!");
      },
      () => {
        this.loading = false;
        this.openSnackBar("Szállítási mód frissítve!");
      }
    )
  }

  openSnackBar(message: string) {
    this.snackBar.open(message, 'OK', {
      duration: 2000,
      horizontalPosition: this.horizontalPosition,
      verticalPosition: this.verticalPosition,
    });
  }

  checkout() {
    this.loading = true;
    this.checkoutSub = this.cartService.canCheckout().subscribe(
      (response: boolean) => {
        this.loading = false;
        if (response) {
          this.router.navigate(['/checkout']);
        } else {
          this.openSnackBar("Valami hiba történt!");
        }
      }, (error) => {
        console.log(error);
        this.loading = false;
        this.openSnackBar("Valami hiba történt!");
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
