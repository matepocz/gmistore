import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {BreakpointObserver, Breakpoints, MediaMatcher} from '@angular/cdk/layout';
import {Observable, Subscription} from 'rxjs';
import {map, shareReplay} from 'rxjs/operators';
import {AuthService} from "../../service/auth-service";
import {CartService} from "../../service/cart-service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-side-nav',
  templateUrl: './side-nav.component.html',
  styleUrls: ['./side-nav.component.css']
})
export class SideNavComponent implements OnInit {
  mobileQuery: MediaQueryList;

  private readonly _mobileQueryListener: () => void;
  opened: boolean = false;

  itemsInCart: number = 0;
  favoriteItems: number = 0;

  authenticatedUser: boolean;
  isAdmin: boolean = false;
  isSeller: boolean = false;

  cartSubscription: Subscription;
  itemsInCartSubscription: Subscription;
  isAdminSub: Subscription;
  isSellerSub: Subscription;

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  constructor(private authService: AuthService, private breakpointObserver: BreakpointObserver,
              changeDetectorRef: ChangeDetectorRef, media: MediaMatcher, private cartService: CartService,
              private router: Router) {
    this.mobileQuery = media.matchMedia('(max-width: 600px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
  }

  ngOnInit() {
    this.authenticatedUser = this.authService.isAuthenticated();
    this.isAdminSub = this.authService.isAdmin.subscribe(
      (response) => {
        this.isAdmin = response;
      }, (error) => {
        console.log(error);
      }
    );

    this.isSellerSub = this.authService.isSeller.subscribe(
      (response) => {
        this.isSeller = response;
      }, (error) => {
        console.log(error);
      }
    );
    this.updateItemsInCart(2);
  }

  logout() {
    this.authService.logout().subscribe(
      () => {
      },
      error => console.log(error),
      () => {
        this.authenticatedUser = false;
        this.updateItemsInCart(0);
        this.router.navigateByUrl('/');
      }
    );
  }

  setUserLoggedIn() {
    this.authenticatedUser = true;
  }

  updateItemsInCart(timeout: number) {
    setTimeout(() => {
        this.cartService.getNumberOfItemsInCart().subscribe(
          (response) => {
            this.itemsInCart = response;
          }
        );
      },
      timeout * 1000);
  }

  ngOnDestroy(): void {
    this.cartSubscription.unsubscribe();
    this.itemsInCartSubscription.unsubscribe();
    this.isAdminSub.unsubscribe();
    this.isSellerSub.unsubscribe();
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }
}
