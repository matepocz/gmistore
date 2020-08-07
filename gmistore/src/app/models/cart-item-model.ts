import {Product} from "./product";

export interface CartItemModel {

  id: number;
  product: Product;
  count: number;
}
