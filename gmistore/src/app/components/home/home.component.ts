import {Component, OnInit} from '@angular/core';
import {OwlOptions} from 'ngx-owl-carousel-o';
import {ProductService} from "../../service/product-service";
import {Title} from "@angular/platform-browser";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  customOptions: OwlOptions = {
    loop: true,
    autoplay: true,
    responsiveRefreshRate: 200,
    margin: 20,
    autoplayTimeout: 5000,
    autoWidth: true,
    mouseDrag: true,
    touchDrag: true,
    pullDrag: false,
    dots: false,
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
        center:false,
        nav: false,
      },
      1500: {
        items: 5,
        center:false,
        nav: false,
      }
    },
  }


  imageURLs = Array<string>();
  temp = Array<string>();

  constructor(private productService: ProductService, private titleService: Title) {
    this.productService.getDiscountProductsProductURL().subscribe(
      (response) => {
        this.imageURLs = response;

        console.log(this.imageURLs);
      }, error => {
        console.log(error);
      }, () => {
        for (let i = 0; i < this.imageURLs.length; i++) {
          if (this.imageURLs[i].includes("?")) {
            let count = this.imageURLs[i].indexOf("?");
            this.temp.push(this.imageURLs[i].substring(0, count));
          } else {
            this.temp.push(this.imageURLs[i]);
          }
        }
      }
    );
  }

  ngOnInit(): void {
    this.titleService.setTitle("Főoldal - GMI Store");
  }

}
