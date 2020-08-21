import {Component, OnDestroy, OnInit} from '@angular/core';
import {ProductService} from "../../service/product-service";
import {ProductModel} from "../../models/product-model";
import {ActivatedRoute, Router} from "@angular/router";
import {RatingModel} from "../../models/rating-model";
import {CartService} from "../../service/cart-service";
import {AuthService} from "../../service/auth-service";
import {LocalStorageService} from "ngx-webstorage";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";
import {Title} from "@angular/platform-browser";
import {ClickEvent} from "angular-star-rating";
import {Subscription} from "rxjs";
import {RatingService} from "../../service/rating.service";

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
  ratingInputValue: number = 0;
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
  ratingSubscription: Subscription;

  constructor(private route: ActivatedRoute, private router: Router, private productService: ProductService,
              private cartService: CartService, private authService: AuthService,
              private localStorageService: LocalStorageService, private snackBar: MatSnackBar,
              private titleService: Title, private ratingService: RatingService) {
  }

  ngOnInit(): void {
    this.slug = this.route.snapshot.params['slug'];

    this.productSubscription = this.productService.getProductBySlug(this.slug).subscribe(
      data => {
        this.product = data;
        this.defaultPicture = data.pictureUrl;
      }, error => console.log(error),
      () => {
        this.titleService.setTitle(this.product.name + " - GMI Store")
      }
    );

    this.ratingSubscription = this.ratingService.getRatingsByProductSlug(this.slug)
      .subscribe(
        (data) => {
          console.log(data);
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
    this.cartService.addProduct(id).subscribe(
      (response) => {
        this.openSnackBar("Termék a kosárba került!")
      }, (error) => {
        console.log(error);
        this.openSnackBar("Valami hiba történt!")
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

  confirmSelection(event: KeyboardEvent) {
    if (event.keyCode === 13 || event.key === 'Enter') {
      this.ratedByCurrentUser = true;
    }
  }

  clickedSelection(event: MouseEvent) {
    this.ratedByCurrentUser = true;
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

  getRatingInputValue = ($event: ClickEvent) => {
    this.ratingInputValue = $event.rating;
  }

  ngOnDestroy() {
    this.productSubscription.unsubscribe();
    this.ratingSubscription.unsubscribe();
  }
}
