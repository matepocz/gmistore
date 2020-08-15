import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../service/product-service";
import {ProductModel} from "../../models/product-model";
import {ActivatedRoute, Router} from "@angular/router";
import {RatingModel} from "../../models/rating-model";
import {CartService} from "../../service/cart-service";
import {AuthService} from "../../user/auth/auth.service";

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
  currentRating: 0;
  maxRating: 5;
  reviewedAlready = false;
  authenticatedUser = this.authService.isAuthenticated();

  constructor(private route: ActivatedRoute, private router: Router, private productService: ProductService,
              private cartService: CartService, private authService: AuthService) {
  }

  ngOnInit(): void {
    this.slug = this.route.snapshot.params['slug'];

    this.productService.getProductBySlug(this.slug).subscribe(
      data => {
        this.product = data;
        this.defaultPicture = data.pictureUrl;
        this.averageRatingPercentage = (100 / 5) * this.product.averageRating;
        this.ratings = this.product.ratings;
      }, error => console.log(error)
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
}
