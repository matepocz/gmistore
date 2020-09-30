import {ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {BreakpointObserver, Breakpoints, MediaMatcher} from '@angular/cdk/layout';
import {Observable, Subscription} from 'rxjs';
import {debounceTime, map, shareReplay} from 'rxjs/operators';
import {AuthService} from "../../service/auth-service";
import {CartService} from "../../service/cart-service";
import {Router} from "@angular/router";
import {MainCategoryModel} from "../../models/main-category.model";
import {AdminService} from "../../service/admin.service";
import {UserService} from "../../service/user.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ProductService} from "../../service/product-service";

@Component({
  selector: 'app-side-nav',
  templateUrl: './side-nav.component.html',
  styleUrls: ['./side-nav.component.css']
})

export class SideNavComponent implements OnInit {

  searchForm: FormGroup = new FormGroup({
    searchInput: new FormControl(null, Validators.required)
  })
  mobileQuery: MediaQueryList;
  @ViewChild('drawer') drawer: ElementRef;

  private readonly _mobileQueryListener: () => void;
  opened: boolean = false;

  categories: Array<MainCategoryModel>;

  itemsInCart: number = 0;
  favoriteItems: number = 0;

  authenticatedUser: boolean;
  currentUsername: string;
  isAdmin: boolean = false;
  isSeller: boolean = false;

  currentSearchingQuery: string;
  autoCompleteOptions: Observable<string[]>;

  subscriptions: Subscription = new Subscription();

  cartSubscription: Subscription;

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  constructor(private authService: AuthService, private breakpointObserver: BreakpointObserver,
              changeDetectorRef: ChangeDetectorRef, media: MediaMatcher, private cartService: CartService,
              private router: Router, private adminService: AdminService, private cdRef: ChangeDetectorRef,
              private userService: UserService, private productService: ProductService) {
    this.mobileQuery = media.matchMedia('(max-width: 600px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
  }

  ngOnInit() {
    this.subscriptions.add(
      this.authService.usernameSubject.subscribe(
        (response) => {
          this.currentUsername = response;
        }, (error) => {
          console.log(error);
        }
      )
    );
    this.subscriptions.add(this.adminService.getProductCategories().subscribe(
      (response: Array<MainCategoryModel>) => {
        this.categories = response;
      }, (error) => {
        console.log(error);
      }
    ));

    this.subscriptions.add(
      this.searchForm.get('searchInput').valueChanges.subscribe(
        (value) => {
          this.currentSearchingQuery = value;
          if (this.currentSearchingQuery !== '') {
            this.autoCompleteOptions = this.fetchSearchOptions();
          }
        }, error => {
          console.log(error)
        }
      )
    );

    this.authenticatedUser = this.authService.isAuthenticated();
    console.log(this.authenticatedUser);
    this.subscriptions.add(this.authService.isAdmin.subscribe(
      (response) => {
        this.isAdmin = response;
      }, (error) => {
        console.log(error);
      }
    ));

    this.subscriptions.add(this.authService.isSeller.subscribe(
      (response) => {
        this.isSeller = response;
      }, (error) => {
        console.log(error);
      }
    ));
    this.updateItemsInCart(2);
    this.updateFavoriteItems(2);
  }

  fetchSearchOptions(): Observable<string[]> {
    return this.productService.getProductNamesForAutocomplete(this.currentSearchingQuery).pipe(
      debounceTime(500),
      map(result => {
          return result;
        }
      ));
  }

  logout() {
    this.subscriptions.add(this.authService.logout().subscribe(
      () => {
      },
      error => console.log(error),
      () => {
        this.authenticatedUser = false;
        this.updateItemsInCart(0);
        this.router.navigateByUrl('/');
      }
    ));
  }

  setUserLoggedIn() {
    this.authenticatedUser = true;
  }

  updateItemsInCart(timeout: number) {
    setTimeout(() => {
        this.subscriptions.add(this.cartService.getNumberOfItemsInCart().subscribe(
          (response) => {
            this.itemsInCart = response;
          }
        ));
      },
      timeout * 1000);
  }

  updateFavoriteItems(timeout: number) {
    if (this.authenticatedUser) {
      setTimeout(() => {
          this.subscriptions.add(this.userService.getCountOfFavoriteProducts().subscribe(
            (response) => {
              this.favoriteItems = response;
            }
          ));
        },
        timeout * 1000);
    }
  }

  getData(selected) {
    let mainCategoryModel = this.categories.find(item => item.key === selected.key);
    return {data: mainCategoryModel.subCategories};
  }

  closeNav() {
    this.opened = false;
  }

  openNav() {
    this.opened = true;
  }

  submitSearch() {
    this.router.navigate(
      ['/product-list/search'],
      {
        queryParams: {
          q: this.searchForm.get('searchInput').value,
          pageSize: 10,
          pageIndex: 0
        }
      }
    )
  }

  detectChanges() {
    this.cdRef.detectChanges();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }
}
