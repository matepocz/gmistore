import {Component, OnDestroy, OnInit} from '@angular/core';
import {ProductModel} from "../../models/product-model";
import {ProductService} from "../../service/product-service";
import {CartService} from "../../service/cart-service";
import {Subscription} from "rxjs";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit, OnDestroy {

  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'bottom';

  products: Array<ProductModel>;

  productsSubscription: Subscription;
  addToCartSubscription: Subscription;

  constructor(private productService: ProductService, private cartService: CartService,
              private snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.products = new Array<ProductModel>();
    this.productsSubscription = this.productService.getActiveProducts().subscribe(
      (data) => {
        this.products = data;
      }, (error) => console.log(error)
    );
  }

  calculateDiscountedPrice(product: ProductModel): number {
    let actualProduct = this.products.find(prod => prod === product);
    return (actualProduct.price / 100) * (100 - actualProduct.discount);
  }

  addToCart(id: number) {
    this.addToCartSubscription = this.cartService.addProduct(id).subscribe(
      (response) => {
        this.openSnackBar('A termék a kosárba került!');
      }, (error) => {
        console.log(error);
        this.openSnackBar("Valami hiba történt!");
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

  ngOnDestroy() {
    this.productsSubscription.unsubscribe();
    if (this.addToCartSubscription) {
      this.addToCartSubscription.unsubscribe();
    }
  }
}
