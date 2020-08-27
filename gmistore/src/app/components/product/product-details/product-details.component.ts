import {Component, OnDestroy, OnInit} from '@angular/core';
import {ProductService} from "../../../service/product-service";
import {ProductModel} from "../../../models/product-model";
import {ActivatedRoute, Router} from "@angular/router";
import {RatingModel} from "../../../models/rating-model";
import {CartService} from "../../../service/cart-service";
import {AuthService} from "../../../service/auth-service";
import {LocalStorageService} from "ngx-webstorage";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";
import {Title} from "@angular/platform-browser";
import {Subscription} from "rxjs";
import {RatingService} from "../../../service/rating.service";
import {SideNavComponent} from "../../side-nav/side-nav.component";

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit, OnDestroy {

  slug: string;
  product: ProductModel;
  defaultPicture: string;

  ratings: Array<RatingModel>;
  ratingByCurrentUser = 0;
  ratedByCurrentUser = false;

  authenticatedUser = this.authService.isAuthenticated();

  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'bottom';

  fiveStars = 0;
  fourStars = 0;
  threeStars = 0;
  twoStars = 0;
  oneStar = 0;

  fiveStarPercentage = 0;
  fourStarPercentage = 0;
  threeStarPercentage = 0;
  twoStarPercentage = 0;
  oneStarPercentage = 0;

  productSubscription: Subscription;
  addToCartSubscription: Subscription;
  ratingSubscription: Subscription;
  removeRatingSubscription: Subscription;

  constructor(private route: ActivatedRoute, private router: Router, private productService: ProductService,
              private cartService: CartService, private authService: AuthService,
              private localStorageService: LocalStorageService, private snackBar: MatSnackBar,
              private titleService: Title, private ratingService: RatingService,
              private sideNavComponent: SideNavComponent) {
  }

  ngOnInit(): void {
    this.slug = this.route.snapshot.params['slug'];

    this.productSubscription = this.productService.getProductBySlug(this.slug).subscribe(
      data => {
        this.product = data;
        this.defaultPicture = data.pictureUrl;
      }, error => console.log(error),
      // ha nem talála a product-ot akkor dobaj át a 404-es oldalra
      //error msg-ből kiszedni és az alapján
      () => {
        this.titleService.setTitle(this.product.name + " - GMI Store")
      }
    );

    this.ratingSubscription = this.ratingService.getRatingsByProductSlug(this.slug)
      .subscribe(
        (data) => {
          this.ratings = data;
        }, (error) => {
          console.log(error);
        },
        () => {
          this.isReviewedByUserAlready();
          this.countRatings();
          this.fiveStarPercentage = this.calculateRatingPercentage(this.fiveStars);
          this.fourStarPercentage = this.calculateRatingPercentage(this.fourStars);
          this.threeStarPercentage = this.calculateRatingPercentage(this.threeStars);
          this.twoStarPercentage = this.calculateRatingPercentage(this.twoStars);
          this.oneStarPercentage = this.calculateRatingPercentage(this.oneStar);
        }
      );
  }

  changeDefaultImg(picture: string): void {
    this.defaultPicture = this.product.pictures.find(x => x === picture);
  }

  calculateDiscountedPrice(): number {
    return (this.product.price / 100) * (100 - this.product.discount);
  }

  addToCart(id: number) {
    this.addToCartSubscription = this.cartService.addProduct(id).subscribe(
      (response) => {
        this.openSnackBar("Termék a kosárba került!");
        this.sideNavComponent.updateItemsInCart(0);
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

  isReviewedByUserAlready() {
    this.ratings.forEach((rating) => {
      if (rating.username === this.localStorageService.retrieve('username')) {
        this.ratingByCurrentUser = rating.actualRating;
        this.ratedByCurrentUser = true;
      }
    })
  }

  countRatings() {
    this.ratings.forEach(
      (rating) => {
        switch (rating.actualRating) {
          case 5:
            this.fiveStars++;
            break;
          case 4:
            this.fourStars++;
            break;
          case 3:
            this.threeStars++;
            break;
          case 2:
            this.twoStars++;
            break;
          case 1:
            this.oneStar++;
            break;
        }
      }
    )
  }

  calculateRatingPercentage(stars: number): number {
    let width = stars / this.ratings?.length;
    return width * 100;
  }

  navigateToRateProduct() {
    this.router.navigate(['/add-review', this.slug])
  }

  openDialog() {

  }

  removeRating(id: number) {
    this.removeRatingSubscription = this.ratingService.removeRating(id).subscribe(
      (response) => {
        if (response === true) {
          this.openSnackBar("Értékelés törölve!");
        } else {
          this.openSnackBar("Nincs jogosultságod a művelethez");
        }
      }, error => console.log(error)
    )
  }

  ngOnDestroy() {
    this.productSubscription.unsubscribe();
    if (this.addToCartSubscription) {
      this.addToCartSubscription.unsubscribe();
    }
    this.ratingSubscription.unsubscribe();
    if (this.removeRatingSubscription) {
      this.removeRatingSubscription.unsubscribe();
    }
  }
}
