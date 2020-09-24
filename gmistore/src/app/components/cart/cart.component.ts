import {Component, OnDestroy, OnInit} from '@angular/core';
import {CartService} from "../../service/cart-service";
import {CartModel} from "../../models/cart-model";
import {Subscription} from "rxjs";
import {ShippingMethodModel} from "../../models/shipping-method-model";
import {Title} from "@angular/platform-browser";
import {SideNavComponent} from "../side-nav/side-nav.component";
import {Router} from "@angular/router";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";
import {SpinnerService} from "../../service/spinner-service.service";
import {MatDialogRef} from "@angular/material/dialog";
import {LoadingSpinnerComponent} from "../loading-spinner/loading-spinner.component";
import {UserService} from "../../service/user.service";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit, OnDestroy {

  loading = false;
  spinner: MatDialogRef<LoadingSpinnerComponent> = this.spinnerService.start();
  cart: CartModel;
  shippingData: ShippingMethodModel[];
  currentShipping: ShippingMethodModel = null;
  expectedDeliveryDate: string;

  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'bottom';

  subscriptions: Subscription = new Subscription();

  constructor(private cartService: CartService, private titleService: Title,
              private sideNavComponent: SideNavComponent, private router: Router,
              private snackBar: MatSnackBar, private spinnerService: SpinnerService,
              private userService: UserService) {
  }

  ngOnInit(): void {
    this.titleService.setTitle("Kosár - GMI Store")
    this.fetchCartData();
    this.subscriptions.add(this.cartService.getShippingData().subscribe(
      (data: Array<ShippingMethodModel>) => {
        this.shippingData = data;
      }, (error) => console.log(error)
    ));
  }

  private fetchCartData() {
    this.subscriptions.add(this.cartService.getCart().subscribe(
      (data) => {
        this.cart = data;
        this.currentShipping = data.shippingMethod;
        this.formatExpectedDeliveryDate(data.expectedDeliveryDate);
      }, (error) => {
        console.log(error);
        this.spinnerService.stop(this.spinner);
      }, () => {
        this.spinnerService.stop(this.spinner);
      }
    ));
  }

  private formatExpectedDeliveryDate(date: Date) {
    let dateString = new Intl.DateTimeFormat('hu-HU',
      {month: 'long', day: '2-digit', weekday: 'long'})
      .format(new Date(date)).toString();
    this.expectedDeliveryDate = dateString.replace(',', ' (') + ' )';
  }

  refreshProductCount(id: number, count: string) {
    this.spinner = this.spinnerService.start("Egy pillanat...");
    this.subscriptions.add(this.cartService.refreshProductCount(id, +count).subscribe(
      (response: boolean) => {
        this.fetchCartData();
        this.openSnackBar("Mennyiség frissítve!");
        this.sideNavComponent.updateItemsInCart(0);
      }, (error) => {
        console.log(error);
        this.spinnerService.stop(this.spinner);
        this.openSnackBar("Valami hiba történt!");
      }, () => this.spinnerService.stop(this.spinner)
    ));
  }

  removeProduct(id: number) {
    this.spinner = this.spinnerService.start();
    this.subscriptions.add(this.cartService.removeProduct(id).subscribe(
      (response: boolean) => {
        if (response) {
          this.openSnackBar("Termék törölve!");
        }
        this.fetchCartData();
        this.sideNavComponent.updateItemsInCart(0);
      }, (error) => {
        console.log(error);
        this.spinnerService.stop(this.spinner);
        this.openSnackBar("Valami hiba történt!");
      }, () => this.spinnerService.stop(this.spinner)
    ));
  }

  updateShippingMethod() {
    this.spinner = this.spinnerService.start();
    this.subscriptions.add(this.cartService.updateShippingMethod(this.currentShipping.method).subscribe(
      () => {
        this.fetchCartData();
      }, error => {
        console.log(error);
        this.spinnerService.stop(this.spinner);
        this.openSnackBar("Valami hiba történt!");
      },
      () => {
        this.spinnerService.stop(this.spinner);
        this.openSnackBar("Szállítási mód frissítve!");
      }
    ));
  }

  openSnackBar(message: string) {
    this.snackBar.open(message, 'OK', {
      duration: 2000,
      horizontalPosition: this.horizontalPosition,
      verticalPosition: this.verticalPosition,
    });
  }

  checkout() {
    this.spinner = this.spinnerService.start();
    this.subscriptions.add(this.cartService.canCheckout().subscribe(
      (response: boolean) => {
        this.loading = false;
        if (response) {
          this.router.navigate(['/checkout']);
        } else {
          this.openSnackBar("Valami hiba történt!");
        }
        this.spinnerService.stop(this.spinner);
      }, (error) => {
        console.log(error);
        this.spinnerService.stop(this.spinner);
        this.openSnackBar("Valami hiba történt!");
      }
    ));
  }

  addProductToFavorites(id: number) {
    this.spinner = this.spinnerService.start();
    this.subscriptions.add(this.userService.addProductToFavorites(id).subscribe(
      (response: boolean) => {
        if (response) {
          this.openSnackBar("Termék hozzáadva a kedvencekhez.");
        } else {
          this.openSnackBar("Valami hiba történt!");
        }
        this.sideNavComponent.updateFavoriteItems(0);
        this.spinnerService.stop(this.spinner);
      }, (error) => {
        this.openSnackBar("Valami hiba történt!");
        this.spinnerService.stop(this.spinner);
      }
    ));
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}
