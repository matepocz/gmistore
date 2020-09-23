import {ChangeDetectorRef, Component, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ProductModel} from "../../../models/product-model";
import {ProductService} from "../../../service/product-service";
import {CartService} from "../../../service/cart-service";
import {Subscription} from "rxjs";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";
import {Title} from "@angular/platform-browser";
import {SideNavComponent} from "../../side-nav/side-nav.component";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {SpinnerService} from "../../../service/spinner-service.service";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {LoadingSpinnerComponent} from "../../loading-spinner/loading-spinner.component";
import {FormBuilder, FormGroup} from "@angular/forms";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {PagedProductListModel} from "../../../models/product/paged-product-list.model";
import {ProductFilterOptions} from "../../../models/product/product-filter-options";
import {AuthService} from "../../../service/auth-service";
import {ConfirmDialog} from "../../confirm-delete-dialog/confirm-dialog";

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css'],
})
export class ProductListComponent implements OnInit, OnDestroy {

  @Input() products: Array<ProductModel>;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  chooseAbleRatings: Array<number> = [5, 4, 3, 2, 1];

  priceForm: FormGroup = this.fb.group({
    minimumPrice: [1],
    maximumPrice: [0]
  })

  notInStock: boolean = false;
  nonDiscounted: boolean = false;
  discounted: boolean = false;
  minPrice: number = 1;
  maxPrice: number = 1;
  minimumPrice: number = 1;
  maximumPrice: number = 1;
  lowestRating: number = 0;
  filterOptions: ProductFilterOptions = new ProductFilterOptions();
  filtering: boolean = false;

  numberOfProducts = 0;
  pageIndex: number = 0;
  pageSize: number = 10;
  pageSizeOptions: Array<number> = [10, 20, 50];

  spinner: MatDialogRef<LoadingSpinnerComponent> = this.spinnerService.start();
  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'bottom';

  category: string;
  categoryDisplayName: string;

  isAdmin: boolean = false;
  currentUsername: string = null;

  productsSubscription: Subscription;
  addToCartSubscription: Subscription;
  paramsSubscription: Subscription;
  adminSub: Subscription;

  constructor(private productService: ProductService, private cartService: CartService,
              private snackBar: MatSnackBar, private titleService: Title,
              private sideNavComponent: SideNavComponent, private activatedRoute: ActivatedRoute,
              private spinnerService: SpinnerService, private fb: FormBuilder,
              private router: Router, private cdRef: ChangeDetectorRef, private authService: AuthService,
              private dialog: MatDialog) {
    this.products = new Array<ProductModel>();
  }

  ngOnInit(): void {
    this.titleService.setTitle("Termékek - GMI Store");
    this.adminSub = this.authService.isAdmin.subscribe(
      (response) => {
        this.isAdmin = response;
      }, error => console.log(error)
    );
    this.currentUsername = this.authService.currentUsername;

    this.paramsSubscription = this.activatedRoute.queryParamMap.subscribe(
      (params: ParamMap) => {
        this.spinnerService.stop(this.spinner);
        this.category = params.get('category');
        this.pageIndex = Number(params.get('pageIndex'));
        this.pageSize = Number(params.get('pageSize'));
        if (this.filtering && this.category) {
          this.fetchProductsByCategory(this.filterOptions);
        } else {
          this.fetchProductsByCategory();
        }
      }, (error) => {
        console.log(error);
      }, () => {
      }
    );
  }

  private fetchProductsByCategory(filterOptions?: ProductFilterOptions) {
    if (this.category) {
      this.spinner = this.spinnerService.start();
      this.productsSubscription = this.productService.getProductsByCategory(
        this.category, this.pageIndex, this.pageSize, filterOptions
      )
        .subscribe(
          (response: PagedProductListModel) => {
            this.products = response.products;
            this.categoryDisplayName = response.categoryDisplayName;
            this.numberOfProducts = response.totalElements;
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
          this.priceForm.patchValue({
            maximumPrice: product.price
          })
        }
      }
    );
  }

  paginationEventHandler($event: PageEvent) {
    this.pageSize = $event.pageSize;
    this.pageIndex = $event.pageIndex;
    this.router.navigate(['.'], {
      relativeTo: this.activatedRoute,
      queryParams: {
        filter: this.filtering,
        category: this.category,
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
      }
    });
  }

  filterProducts() {
    this.filtering = true;
    this.setFilterOptions();
    this.router.navigate(['.'], {
      relativeTo: this.activatedRoute,
      queryParams: {
        filter: this.filtering,
        category: this.category,
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
      }
    });
    this.fetchProductsByCategory(this.filterOptions);
  }

  private setFilterOptions() {
    this.filterOptions.notInStock = this.notInStock;
    this.filterOptions.nonDiscounted = this.nonDiscounted;
    this.filterOptions.discounted = this.discounted;
    this.filterOptions.minPrice = this.minimumPrice;
    this.filterOptions.maxPrice = this.maximumPrice;
    this.filterOptions.lowestRating = this.lowestRating;
  }

  removeFilters() {
    this.filtering = false;
    this.notInStock = false;
    this.nonDiscounted = false;
    this.discounted = false;
    this.lowestRating = 0;
    this.router.navigate(['.'], {
      relativeTo: this.activatedRoute,
      queryParams: {
        filter: this.filtering,
        category: this.category,
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
      }
    });
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

  updateMinimumPriceSlider(value: string) {
    this.minimumPrice = Number(value);
  }

  updateMaximumPriceSlider(value: string) {
    this.maximumPrice = Number(value);
  }

  openDeleteProductDialog(productId: number, productName?: string) {
    const dialogRef = this.dialog.open(ConfirmDialog, {
      width: '250px',
      data: {
        message: 'Biztosan törölni szeretnéd?',
        name: productName
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleteProduct(productId);
      }
    });
  }

  deleteProduct(id: number) {
    this.productService.deleteProduct(id).subscribe(
      (response) => {
        if (response) {
          if (this.filtering) {
            this.fetchProductsByCategory(this.filterOptions);
          } else {
            this.fetchProductsByCategory();
          }
        }
      }, error => console.log(error)
    )
  }

  detectChanges() {
    this.cdRef.detectChanges();
  }

  ngOnDestroy() {
    if (this.productsSubscription) {
      this.productsSubscription.unsubscribe();
    }
    this.paramsSubscription.unsubscribe();
    if (this.addToCartSubscription) {
      this.addToCartSubscription.unsubscribe();
    }
  }
}
