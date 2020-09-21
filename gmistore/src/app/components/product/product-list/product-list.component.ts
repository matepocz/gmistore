import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {ProductModel} from "../../../models/product-model";
import {ProductService} from "../../../service/product-service";
import {CartService} from "../../../service/cart-service";
import {Subscription} from "rxjs";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";
import {Title} from "@angular/platform-browser";
import {SideNavComponent} from "../../side-nav/side-nav.component";
import {ActivatedRoute} from "@angular/router";
import {SpinnerService} from "../../../service/spinner-service.service";
import {MatDialogRef} from "@angular/material/dialog";
import {LoadingSpinnerComponent} from "../../loading-spinner/loading-spinner.component";

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css'],
})
export class ProductListComponent implements OnInit, OnDestroy {

  @Input() products: Array<ProductModel>;

  spinner: MatDialogRef<LoadingSpinnerComponent> = this.spinnerService.start();
  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'bottom';

  category: string;

  minPrice: number;
  maxPrice: number;

  minimumPrice: number = 1;
  maximumPrice: number = 1;

  productsSubscription: Subscription;
  addToCartSubscription: Subscription;
  paramsSubscription: Subscription;

  constructor(private productService: ProductService, private cartService: CartService,
              private snackBar: MatSnackBar, private titleService: Title,
              private sideNavComponent: SideNavComponent, private activatedRoute: ActivatedRoute,
              private spinnerService: SpinnerService) {
    this.products = new Array<ProductModel>();
  }

  ngOnInit(): void {
    this.titleService.setTitle("Termékek - GMI Store");
    this.paramsSubscription = this.activatedRoute.queryParamMap.subscribe(
      (params) => {
        this.spinnerService.stop(this.spinner);
        this.category = params.get('category');
        this.fetchProductsByCategory();
      }, (error) => {
        console.log(error);
      }, () => {
      }
    );
  }

  private fetchProductsByCategory() {
    if (this.category) {
      this.spinner = this.spinnerService.start();
      this.productsSubscription = this.productService.getProductsByCategory(this.category).subscribe(
        (response: Array<ProductModel>) => {
          this.products = response;
          this.setMinAndMaxPrices();
          this.spinnerService.stop(this.spinner);
        }, error => {
          console.log(error)
          this.spinnerService.stop(this.spinner);
        }
      )
    }
  }

  setMinAndMaxPrices() {
    this.products.forEach(
      (product: ProductModel) => {
        if (product.price > this.maximumPrice) {
          this.maximumPrice = product.price;
          this.maxPrice = product.price;
        }
        if (product.price < this.minimumPrice) {
          this.minimumPrice = product.price;
          this.minPrice = product.price;
        }
      }
    );
  }

  calculateDiscountedPrice(product: ProductModel): number {
    let actualProduct = this.products.find(prod => prod === product);
    return (actualProduct.price / 100) * (100 - actualProduct.discount);
  }

  addToCart(id: number) {
    this.spinner = this.spinnerService.start();
    this.addToCartSubscription = this.cartService.addProduct(id).subscribe(
      (response) => {
        if (response) {
          this.openSnackBar('A termék a kosárba került!');
          this.sideNavComponent.updateItemsInCart(0);
        } else {
          this.openSnackBar("A kért mennyiség nincs készleten!");
        }
        this.spinnerService.stop(this.spinner);
      }, (error) => {
        console.log(error);
        this.spinnerService.stop(this.spinner);
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


  formatLabel(value: number) {
    if (value >= 1000) {
      return Math.round(value / 1000) + 'k';
    }
    return value;
  }

  updateMinimumPrice(minPriceElement: HTMLInputElement) {
    minPriceElement.value = this.minimumPrice.toString();
  }

  updateMaximumPrice(maxPriceElement: HTMLInputElement) {
    maxPriceElement.value = this.maximumPrice.toString();
  }

  ngOnDestroy() {
    this.productsSubscription.unsubscribe();
    this.paramsSubscription.unsubscribe();
    if (this.addToCartSubscription) {
      this.addToCartSubscription.unsubscribe();
    }
  }
}
