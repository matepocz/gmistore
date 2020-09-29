import {Component, OnDestroy, OnInit} from '@angular/core';
import {UserService} from "../../service/user.service";
import {ProductModel} from "../../models/product-model";
import {Subscription} from "rxjs";
import {SpinnerService} from "../../service/spinner-service.service";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {LoadingSpinnerComponent} from "../loading-spinner/loading-spinner.component";
import {ConfirmDialog} from "../confirm-delete-dialog/confirm-dialog";
import {PopupSnackbar} from "../../utils/popup-snackbar";
import {CartService} from "../../service/cart-service";
import {SideNavComponent} from "../side-nav/side-nav.component";

@Component({
  selector: 'app-favorite-products',
  templateUrl: './favorite-products.component.html',
  styleUrls: ['./favorite-products.component.css']
})
export class FavoriteProductsComponent implements OnInit, OnDestroy {

  spinner: MatDialogRef<LoadingSpinnerComponent> = this.spinnerService.start();

  products: Array<ProductModel>;

  subscriptions: Subscription = new Subscription();

  constructor(private userService: UserService, private spinnerService: SpinnerService,
              private dialog: MatDialog, private snackbar: PopupSnackbar,
              private cartService: CartService, private sideNav: SideNavComponent) {
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.userService.getFavoriteProducts().subscribe(
        (response: Array<ProductModel>) => {
          this.products = response;
          this.spinnerService.stop(this.spinner);
        }, (error) => {
          console.log(error);
          this.spinnerService.stop(this.spinner);
        }
      )
    );
  }

  openDeleteProductDialog(productId: number, productName?: string) {
    const dialogRef = this.dialog.open(ConfirmDialog, {
      width: '250px',
      data: {
        message: 'Biztosan törölni szeretnéd?',
        name: productName
      }
    });

    this.subscriptions.add(
      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.deleteProductFromFavorites(productId);
        }
      })
    );
  }

  deleteProductFromFavorites(id: number) {
    this.spinner = this.spinnerService.start();
    this.userService.removeProductFromFavorites(id).subscribe(
      (response: boolean) => {
        if (response) {
          this.snackbar.popUp("Termék törölve!");

          this.subscriptions.add(
            this.userService.getFavoriteProducts().subscribe(
              (response) => {
                this.products = response;
              }, (error) => {
                console.log(error);
              }
            )
          );
        }
        this.sideNav.updateFavoriteItems(0);
        this.spinnerService.stop(this.spinner);
      }, (error) => {
        this.spinnerService.stop(this.spinner);
        console.log(error);
      }
    )
  }

  addProductToCart(id: number) {
    this.spinner = this.spinnerService.start();
    this.subscriptions.add(
      this.cartService.addProduct(id).subscribe(
        (response: boolean) => {
          if (response) {
            this.snackbar.popUp("A termék a kosárba került!");
            this.sideNav.updateItemsInCart(0);
          }
          this.spinnerService.stop(this.spinner);
        }, (error) => {
          console.log(error);
          this.spinnerService.stop(this.spinner);
        }
      )
    );
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}
