import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../service/product-service";
import {Product} from "../product";
import {ActivatedRoute, Router} from "@angular/router";
import {Rating} from "../rating";

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit {

  id: number;
  product: Product;
  defaultPicture: string;
  averageRatingPercentage: number;
  ratings: Array<Rating>;

  constructor(private route: ActivatedRoute, private router: Router,
              private productService: ProductService) {
  }

  ngOnInit(): void {
    this.product = new Product();
    this.id = this.route.snapshot.params['id'];

    this.productService.getProduct(this.id).subscribe(
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
}
