import {ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ProductModel} from "../../../models/product-model";
import {ProductService} from "../../../service/product-service";
import {CartService} from "../../../service/cart-service";
import {Subscription} from "rxjs";
import {Title} from "@angular/platform-browser";
import {SideNavComponent} from "../../side-nav/side-nav.component";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {SpinnerService} from "../../../service/spinner-service.service";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {LoadingSpinnerComponent} from "../../loading-spinner/loading-spinner.component";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {PagedProductListModel} from "../../../models/product/paged-product-list.model";
import {ProductFilterOptions} from "../../../models/product/product-filter-options";
import {AuthService} from "../../../service/auth-service";
import {ConfirmDialog} from "../../confirm-delete-dialog/confirm-dialog";
import {UserService} from "../../../service/user.service";
import {PopupSnackbar} from "../../../utils/popup-snackbar";
import {FilterDialogComponent} from "../filter-dialog/filter-dialog.component";

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css'],
})
export class ProductListComponent implements OnInit, OnDestroy {

  products: Array<ProductModel>;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  priceForm: FormGroup = this.fb.group({
    minimumPrice: [1, Validators.compose(
      [Validators.required, Validators.min(1)])],
    maximumPrice: [0, Validators.compose(
      [Validators.required, Validators.min(2)])]
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

  deals: boolean = false;

  searchInput: string;
  searching: boolean = false;

  category: string;
  categoryDisplayName: string;

  isAdmin: boolean = false;
  currentUsername: string = null;

  subscriptions: Subscription = new Subscription();

  constructor(private productService: ProductService, private cartService: CartService,
              private snackBar: PopupSnackbar, private titleService: Title,
              private sideNavComponent: SideNavComponent, private activatedRoute: ActivatedRoute,
              private spinnerService: SpinnerService, private fb: FormBuilder,
              private router: Router, private cdRef: ChangeDetectorRef, private authService: AuthService,
              private dialog: MatDialog, private userService: UserService) {
    this.products = new Array<ProductModel>();
  }

  ngOnInit(): void {
    if (this.activatedRoute.snapshot.url[1]?.path === "deals") {
      this.deals = true;
    } else if (this.activatedRoute.snapshot.url[1]?.path === "search") {
      this.searching = true;
    }
    this.titleService.setTitle("Termékek - GMI Store");

    this.subscriptions.add(
      this.authService.isAdmin.subscribe(
        (response: boolean) => {
          this.isAdmin = response;
        }, error => console.log(error)
      )
    );
    this.currentUsername = this.authService.currentUsername;

    this.subscriptions.add(
      this.activatedRoute.queryParamMap.subscribe(
        (params: ParamMap) => {
          this.spinnerService.stop(this.spinner);
          this.category = params.get('category');
          this.pageIndex = Number(params.get('pageIndex'));
          this.pageSize = Number(params.get('pageSize'));
          this.searchInput = params.get('q');

          if (this.filtering && this.deals) {
            this.fetchDiscountedProducts(this.filterOptions);
          } else if (this.deals) {
            this.fetchDiscountedProducts();
          } else if (this.filtering && this.category) {
            this.fetchProductsByCategory(this.filterOptions);
          } else if (this.category) {
            this.fetchProductsByCategory();
          } else if (this.filtering && this.searching && this.searchInput) {
            this.fetchProductsBySearchInput(this.filterOptions);
          } else if (this.searching && this.searchInput) {
            this.fetchProductsBySearchInput();
          } else {
            //this.router.navigate(['/not-found']);
          }

        }, (error) => {
          console.log(error);
        }
      )
    );
  }

  private fetchProductsBySearchInput(filterOptions?: ProductFilterOptions) {
    this.spinner = this.spinnerService.start();
    this.subscriptions.add(
      this.productService.getProductsBySearchInput(
        this.searchInput, this.pageIndex, this.pageSize, filterOptions
      ).subscribe(
        (response: PagedProductListModel) => {
          this.handlePagedProductListResponse(response);
        }, (error) => {
          console.log(error);
          this.spinnerService.stop(this.spinner);
        }
      )
    );
  }

  private handlePagedProductListResponse(response: PagedProductListModel) {
    this.products = response.products;
    this.setMaxPrice(response.highestPrice);
    this.numberOfProducts = response.totalElements;
    this.categoryDisplayName = response.categoryDisplayName;
    this.titleService.setTitle(this.categoryDisplayName + " - GMI Store");
    this.spinnerService.stop(this.spinner);
  }

  private fetchDiscountedProducts(filterOptions?: ProductFilterOptions) {
    this.spinner = this.spinnerService.start();
    this.subscriptions.add(
      this.productService.getDiscountedProducts(this.pageIndex, this.pageSize, filterOptions).subscribe(
        (response: PagedProductListModel) => {
          this.handlePagedProductListResponse(response);
        }, () => {
          this.spinnerService.stop(this.spinner);
        }
      )
    );
  }

  private fetchProductsByCategory(filterOptions?: ProductFilterOptions) {
    if (this.category) {
      this.spinner = this.spinnerService.start();
      this.subscriptions.add(this.productService.getProductsByCategory(
        this.category, this.pageIndex, this.pageSize, filterOptions)
        .subscribe(
          (response: PagedProductListModel) => {
            this.handlePagedProductListResponse(response);
          }, error => {
            console.log(error)
            this.spinnerService.stop(this.spinner);
          }
        )
      );
    }
  }

  setMaxPrice(maxPrice: number) {
    if (maxPrice > this.maximumPrice && !this.filtering) {
      this.maximumPrice = maxPrice + 1
      this.maxPrice = maxPrice + 1;
      this.priceForm.patchValue({
        maximumPrice: maxPrice + 1,
      });
    }
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
    this.pageIndex = 0;
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
    if (this.deals) {
      this.fetchDiscountedProducts(this.filterOptions);
    } else {
      this.fetchProductsByCategory(this.filterOptions);
    }
  }

  private setFilterOptions() {
    this.filterOptions.notInStock = this.notInStock;
    this.filterOptions.nonDiscounted = this.nonDiscounted;
    if (this.deals) {
      this.filterOptions.discounted = true;
    } else {
      this.filterOptions.discounted = this.discounted;
    }
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
    if (this.category) {
      this.navigateToUnfilteredCategorizedPage();
    } else if (this.deals) {
      this.navigateToUnfilteredDealsPage();
    }
  }

  openFilterDialog() {
    this.setFilterOptions();
    const dialogRef = this.dialog.open(FilterDialogComponent, {
      width: '90%',
      data: {
        filterOptions: this.filterOptions,
        deals: this.deals
      }
    });

    this.subscriptions.add(
      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.notInStock = this.filterOptions.notInStock;
          this.discounted = this.filterOptions.discounted;
          this.nonDiscounted = this.filterOptions.nonDiscounted;
          this.minimumPrice = this.filterOptions.minPrice;
          this.maximumPrice = this.filterOptions.maxPrice;
          this.lowestRating = this.filterOptions.lowestRating;
          this.filterProducts();
        }
      })
    );
  }

  private navigateToUnfilteredCategorizedPage() {
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

  private navigateToUnfilteredDealsPage() {
    this.router.navigate(['.'], {
      relativeTo: this.activatedRoute,
      queryParams: {
        filter: this.filtering,
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
    this.subscriptions.add(this.cartService.addProduct(id).subscribe(
      (response) => {
        if (response) {
          this.snackBar.popUp('A termék a kosárba került!');
          this.sideNavComponent.updateItemsInCart(0);
        } else {
          this.snackBar.popUp("A kért mennyiség nincs készleten!");
        }
        this.spinnerService.stop(this.spinner);
      }, (error) => {
        console.log(error);
        this.spinnerService.stop(this.spinner);
        this.snackBar.popUp("Valami hiba történt!");
      }
    ));
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

    this.subscriptions.add(
      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.deleteProduct(productId);
        }
      })
    );
  }

  deleteProduct(id: number) {
    this.subscriptions.add(this.productService.deleteProduct(id).subscribe(
      (response: boolean) => {
        if (response) {
          this.snackBar.popUp("Termék törölve.");
          if (this.filtering && this.deals) {
            this.fetchDiscountedProducts(this.filterOptions);
          } else if (this.deals) {
            this.fetchDiscountedProducts();
          } else if (this.filtering && this.category) {
            this.fetchProductsByCategory(this.filterOptions);
          } else {
            this.fetchProductsByCategory();
          }
        }
      }, (error) => {
        this.snackBar.popUp("Valami hiba történt!");
        console.log(error)
      }
    ));
  }

  addProductToFavorites(id: number) {
    this.spinner = this.spinnerService.start();
    this.subscriptions.add(this.userService.addProductToFavorites(id).subscribe(
      (response: boolean) => {
        if (response) {
          this.snackBar.popUp("Termék hozzáadva a kedvencekhez.");
          this.sideNavComponent.updateFavoriteItems(0);
        } else {
          this.snackBar.popUp("Valami hiba történt!");
        }
        this.spinnerService.stop(this.spinner);
      }, () => {
        this.snackBar.popUp("Valami hiba történt!");
        this.spinnerService.stop(this.spinner);
      }
    ));
  }

  detectChanges() {
    this.cdRef.detectChanges();
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}
