import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ProductModel} from "../../../models/product-model";

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.css']
})
export class ProductCardComponent implements OnInit {
  @Input() product: ProductModel;
  @Input() deleteAble: boolean = false;
  @Output() deleteProduct: EventEmitter<any> = new EventEmitter<any>();
  @Output() productToCart: EventEmitter<any> = new EventEmitter<any>();

  constructor() {
  }

  ngOnInit(): void {
  }

  deleteProductFromFavorites() {
    this.deleteProduct.emit();
  }

  addProductToCart() {
    this.productToCart.emit();
  }

}
