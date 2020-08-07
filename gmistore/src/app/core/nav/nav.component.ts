import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthService} from "../../user/auth/auth.service";
import {CartService} from "../../service/cart-service";
import {CartModel} from "../../models/cart-model";
import {Subscription} from "rxjs";


@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit, OnDestroy {

  public isCollapsed = true;
  cart: CartModel;
  cartSubscription: Subscription;
  cartItems = 0;

  constructor(public authService: AuthService, private cartService: CartService) {
  }

  ngOnInit(): void {
    this.cartSubscription = this.cartService.getCart().subscribe(
      (data) => {
        this.cart = data;
      }, (error) => {
        console.log(error)
      }, () => {
        this.cartItems = this.cart.cartItems.length;
      }
    );
  }

  logout() {
    this.authService.logout();
  }

  ngOnDestroy() {
    this.cartSubscription.unsubscribe();
  }
}
