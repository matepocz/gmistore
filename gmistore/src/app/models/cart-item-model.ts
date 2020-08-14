import {ProductInCartModel} from "./product-in-cart-model";

export interface CartItemModel {

  id: number;
  product: ProductInCartModel;
  count: number;
}
