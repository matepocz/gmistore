import {Component, OnDestroy, OnInit} from '@angular/core';
import {OwlOptions} from 'ngx-owl-carousel-o';
import {ProductService} from "../../service/product-service";
import {Title} from "@angular/platform-browser";
import {ProductModel} from "../../models/product-model";
import {Router} from "@angular/router";
import {Subscription} from "rxjs";
import {SpinnerService} from "../../service/spinner-service.service";
import {MatDialogRef} from "@angular/material/dialog";
import {LoadingSpinnerComponent} from "../loading-spinner/loading-spinner.component";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {
  customOptions: OwlOptions = {
    loop: true,
    autoplay: true,
    responsiveRefreshRate: 200,
    margin: 20,
    autoplayTimeout: 5000,
    autoWidth: true,
    mouseDrag: false,
    touchDrag: true,
    pullDrag: false,
    dots: true,
    navSpeed: 700,
    navText: ['<<', '>>'],
    responsive: {
      0: {
        items: 2,
        center: false,
        nav: true,
        dots: false
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
        mouseDrag: true,
      }
    },
  }


  products: Array<ProductModel>;
  private productSub: Subscription = new Subscription();
  spinner: MatDialogRef<LoadingSpinnerComponent> = this.spinnerService.start();

  constructor(private productService: ProductService,
              private titleService: Title,
              private router: Router,
              private spinnerService: SpinnerService) {
  }

  ngOnInit(): void {
    this.productSub.add(
      this.productService.getDiscountProducts().subscribe(
        (response) => {
          this.products = response;
          this.spinnerService.stop(this.spinner);
        }, error => {
          this.spinnerService.stop(this.spinner);
        }, () => {
          this.spinnerService.stop(this.spinner);
        }
      )
    );
    this.titleService.setTitle("Főoldal - GMI Store");
  }

  navigateToProduct(slug: string) {
    this.router.navigate(['/product/' + slug]);
  }

  ngOnDestroy(): void {
    this.productSub.unsubscribe();
  }


}
