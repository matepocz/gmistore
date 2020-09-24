import {ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {BreakpointObserver, Breakpoints, MediaMatcher} from '@angular/cdk/layout';
import {Observable, Subscription} from 'rxjs';
import {map, shareReplay} from 'rxjs/operators';
import {AuthService} from "../../service/auth-service";
import {CartService} from "../../service/cart-service";
import {Router} from "@angular/router";
import {MainCategoryModel} from "../../models/main-category.model";
import {AdminService} from "../../service/admin.service";
import {UserService} from "../../service/user.service";

@Component({
  selector: 'app-side-nav',
  templateUrl: './side-nav.component.html',
  styleUrls: ['./side-nav.component.css']
})

export class SideNavComponent implements OnInit {
  mobileQuery: MediaQueryList;
  @ViewChild('drawer') drawer: ElementRef;

  private readonly _mobileQueryListener: () => void;
  opened: boolean = false;

  categories: Array<MainCategoryModel>;

  itemsInCart: number = 0;
  favoriteItems: number = 0;

  authenticatedUser: boolean;
  isAdmin: boolean = false;
  isSeller: boolean = false;

  subscriptions: Subscription = new Subscription();

  cartSubscription: Subscription;

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  constructor(private authService: AuthService, private breakpointObserver: BreakpointObserver,
              changeDetectorRef: ChangeDetectorRef, media: MediaMatcher, private cartService: CartService,
              private router: Router, private adminService: AdminService,
              private userService: UserService) {
    this.mobileQuery = media.matchMedia('(max-width: 600px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
  }

  ngOnInit() {
    this.subscriptions.add(this.adminService.getProductCategories().subscribe(
      (response: Array<MainCategoryModel>) => {
        this.categories = response;
      }, (error) => {
        console.log(error);
      }
    ));

    this.authenticatedUser = this.authService.isAuthenticated();
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
    setTimeout(() => {
        this.subscriptions.add(this.userService.getCountOfFavoriteProducts().subscribe(
          (response) => {
            this.favoriteItems = response;
          }
        ));
      },
      timeout * 1000);
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

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }
}
