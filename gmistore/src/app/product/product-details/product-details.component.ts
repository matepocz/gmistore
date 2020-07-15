import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../utils/product-service";
import {Product} from "../product";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit {

  id: number;
  product: Product;
  defaultPicture: string;

  constructor(private route: ActivatedRoute, private router: Router,
              private productService: ProductService) {
  }

  ngOnInit(): void {
    this.product = new Product();
    this.id = this.route.snapshot.params['id'];

    this.productService.getProduct(this.id).subscribe(
      data => {
        console.log(data)
        this.product = data;
        this.defaultPicture = data.pictureUrl;
      }, error => console.log(error)
    );
  }

  changeDefaultImg(picture: string): void {
    this.defaultPicture = this.product.pictures.find(x => x === picture);
  }
}
