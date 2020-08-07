import {CartItemModel} from "./cart-item-model";

export interface CartModel {
  id: number;
  cartItems: Array<CartItemModel>;
  totalPrice: number;
}
