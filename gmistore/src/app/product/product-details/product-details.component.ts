import {Component, Input, OnInit} from '@angular/core';
import {ProductService} from "../../utils/product-service";
import {Product} from "../product";
import {Router, ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit {

  id: number;
  product: Product;

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
      }, error => console.log(error)
    );
  }

}
