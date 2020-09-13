import {Component, OnDestroy, OnInit} from '@angular/core';
import {ProductModel} from "../../../models/product-model";
import {ProductService} from "../../../service/product-service";
import {CartService} from "../../../service/cart-service";
import {Subscription} from "rxjs";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";
import {Title} from "@angular/platform-browser";
import {SideNavComponent} from "../../side-nav/side-nav.component";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit, OnDestroy {

  loading: boolean = false;
  category: string;

  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'bottom';

  products: Array<ProductModel>;
  minimumPrice: number = 1;
  maximumPrice: number = 1;

  productsSubscription: Subscription;
  addToCartSubscription: Subscription;

  constructor(private productService: ProductService, private cartService: CartService,
              private snackBar: MatSnackBar, private titleService: Title,
              private sideNavComponent: SideNavComponent, private activatedRoute: ActivatedRoute) {
    this.products = new Array<ProductModel>();
  }

  ngOnInit(): void {
    this.loading = true;
    this.titleService.setTitle("Termékek - GMI Store");
    this.activatedRoute.queryParamMap.subscribe(
      (params) => {
        this.category = params.get('category');
        this.fetchProductsByCategory();
        this.loading = false;
      }, (error) => {
        console.log(error);
        this.loading = false;
      }
    );
  }

  private fetchProductsByCategory() {
    if (this.category) {
      this.loading = true;
      this.productsSubscription = this.productService.getProductsByCategory(this.category).subscribe(
        (response: Array<ProductModel>) => {
          this.products = response;
          console.log(response);
          this.setMinAndMaxPrices();
        }, error => console.log(error)
      )
    }
  }

  setMinAndMaxPrices() {
    this.products.forEach(
      (product) => {
        if (product.price > this.maximumPrice) {
          this.maximumPrice = product.price;
        }
        if (product.price < this.minimumPrice) {
          this.minimumPrice = product.price;
        }
      }
    );
  }

  calculateDiscountedPrice(product: ProductModel): number {
    let actualProduct = this.products.find(prod => prod === product);
    return (actualProduct.price / 100) * (100 - actualProduct.discount);
  }

  addToCart(id: number) {
    this.addToCartSubscription = this.cartService.addProduct(id).subscribe(
      (response) => {
        this.sideNavComponent.updateItemsInCart(0);
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

  updateMinimumPrice(minPrice: HTMLInputElement) {
    minPrice.value = this.minimumPrice.toString();
  }

  formatLabel(value: number) {
    if (value >= 1000) {
      return Math.round(value / 1000) + 'k';
    }
    return value;
  }

  updateMaximumPrice(maxPrice: HTMLInputElement) {
    maxPrice.value = this.maximumPrice.toString();
  }

  ngOnDestroy() {
    this.productsSubscription.unsubscribe();
    if (this.addToCartSubscription) {
      this.addToCartSubscription.unsubscribe();
    }
  }
}
