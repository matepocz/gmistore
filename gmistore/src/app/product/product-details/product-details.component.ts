import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../service/product-service";
import {ProductModel} from "../../models/product-model";
import {ActivatedRoute, Router} from "@angular/router";
import {RatingModel} from "../../models/rating-model";
import {CartService} from "../../service/cart-service";
import {AuthService} from "../../service/auth-service";
import {LocalStorageService} from "ngx-webstorage";

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit {

  slug: string;
  product: ProductModel;
  defaultPicture: string;
  averageRatingPercentage: number;
  ratings: Array<RatingModel>;
  currentRating = 0;
  maxRating: 5;
  reviewedAlready = false;
  authenticatedUser = this.authService.isAuthenticated();

  fiveStars = 0;
  fourStars = 0;
  threeStars = 0;
  twoStars = 0;
  oneStar = 0;

  fiveStarPercentage = 0;
  fourStarPercentage = 0;
  threeStarPercentage= 0;
  twoStarPercentage = 0;
  oneStarPercentage = 0;

  constructor(private route: ActivatedRoute, private router: Router, private productService: ProductService,
              private cartService: CartService, private authService: AuthService,
              private localStorageService: LocalStorageService) {
  }

  ngOnInit(): void {
    this.slug = this.route.snapshot.params['slug'];

    this.productService.getProductBySlug(this.slug).subscribe(
      data => {
        this.product = data;
        this.defaultPicture = data.pictureUrl;
        this.averageRatingPercentage = (100 / 5) * this.product.averageRating;
        this.ratings = this.product.ratings;
      }, error => console.log(error),
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

  calculatePercentageForRating(rating: number): number {
    return (100 / 5) * rating;
  }

  addToCart(id: number) {
    this.cartService.addProduct(id).subscribe(
      (response) => {
        console.log(response);
      }, (error) => console.log(error)
    )
  }

  confirmSelection(event: KeyboardEvent) {
    if (event.keyCode === 13 || event.key === 'Enter') {
      this.reviewedAlready = true;
    }
  }

  clickedSelection(event: MouseEvent) {
    this.reviewedAlready = true;
  }

  isReviewedByUserAlready() {
    this.ratings.forEach((rating) => {
      if (rating.username === this.localStorageService.retrieve('username')) {
        this.currentRating = rating.actualRating;
        this.reviewedAlready = true;
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
}
