import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {BreakpointObserver, Breakpoints, MediaMatcher} from '@angular/cdk/layout';
import {Observable, Subscription} from 'rxjs';
import {map, shareReplay} from 'rxjs/operators';
import {AuthService} from "../../service/auth-service";
import {CartService} from "../../service/cart-service";

@Component({
  selector: 'app-side-nav',
  templateUrl: './side-nav.component.html',
  styleUrls: ['./side-nav.component.css']
})
export class SideNavComponent implements OnInit {
  mobileQuery: MediaQueryList;

  private readonly _mobileQueryListener: () => void;
  opened: boolean = false;
  authenticatedUser: boolean;

  cartSubscription: Subscription;
  itemsInCartSubscription: Subscription;

  itemsInCart: number = 0;
  favoriteItems: number = 0;

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  constructor(private authService: AuthService, private breakpointObserver: BreakpointObserver,
              changeDetectorRef: ChangeDetectorRef, media: MediaMatcher, private cartService: CartService) {
    this.mobileQuery = media.matchMedia('(max-width: 600px)');
    this._mobileQueryListener = () => changeDetectorRef.detectChanges();
    this.mobileQuery.addListener(this._mobileQueryListener);
  }

  ngOnInit() {
    this.authenticatedUser = this.authService.isAuthenticated();
    this.updateItemsInCart(2);
  }

  logout() {
    this.authService.logout();
    this.ngOnInit();
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
    this.mobileQuery.removeListener(this._mobileQueryListener);
  }
}
