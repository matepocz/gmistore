import {Component, Input, OnInit} from '@angular/core';
import {ProductOrderedListModel} from "../../../models/product/productOrderedListModel";

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.css']
})
export class ProductCardComponent implements OnInit {
  @Input() products: ProductOrderedListModel[];
  constructor() { }

  ngOnInit(): void {
  }

}
