import {Component, OnInit} from '@angular/core';
import {OwlOptions} from 'ngx-owl-carousel-o';
import {ProductService} from "../../service/product-service";
import {Title} from "@angular/platform-browser";
import {ProductModel} from "../../models/product-model";
import {Router} from "@angular/router";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  customOptions: OwlOptions = {
    loop: true,
    autoplay: false,
    responsiveRefreshRate: 200,
    margin: 20,
    autoplayTimeout: 5000,
    autoWidth: true,
    mouseDrag: false,
    touchDrag: true,
    pullDrag: false,
    dots: true,
    navSpeed: 700,
    navText: ['', ''],
    responsive: {
      0: {
        items: 2,
        center: false,
        nav: false,
      },
      740: {
        items: 3,
        center: false,
        nav: false,
      },
      1000: {
        items: 4,
        center: false,
        nav: false,
      },
      1200: {
        items: 5,
        center: false,
        nav: false,
      },
      1500: {
        items: 5,
        center: false,
        nav: false,
      }
    },
  }


  products: Array<ProductModel>;

  constructor(private productService: ProductService, private titleService: Title,private router:Router) {
    this.productService.getDiscountProducts().subscribe(
      (response) => {
        this.products = response;
        console.log(this.products);
      }, error => {
        // console.log(error);
      }, () => {

      }
    );
  }

  ngOnInit(): void {
    this.titleService.setTitle("Főoldal - GMI Store");
  }

  navigateToProduct(slug: string) {
    this.router.navigate(['/product/'+slug]);
  }
}
