import {Component, OnInit} from '@angular/core';
import {OwlOptions} from 'ngx-owl-carousel-o';
import {ProductService} from "../../service/product-service";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  customOptions: OwlOptions = {
    center:true,
    autoplay:true,
    slideBy:2,
    responsiveRefreshRate:200,
    loop: true,
    margin: 20,
    autoplayTimeout:3000,
    autoWidth:true,
    mouseDrag: true,
    touchDrag: true,
    pullDrag: false,
    dots: false,
    navSpeed: 700,
    navText: ['', ''],
    responsive: {
      0: {
        items: 2,
        nav:false,
        center:false,
      },
      740: {
        items: 3,
        nav:false,
        center:false,
      },
      1281: {
        items: 4,
        nav:false,

      }
    },
  }


  imageURLs = Array<string>();
  temp = Array<string>();

  constructor(private productService: ProductService) {
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

  }

}
