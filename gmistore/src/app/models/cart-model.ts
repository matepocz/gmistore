import {CartItemModel} from "./cart-item-model";
import {ShippingMethodModel} from "./shipping-method-model";

export interface CartModel {
  id: number;
  cartItems: Array<CartItemModel>;
  shippingMethod: ShippingMethodModel;
  itemsTotalPrice: number
  totalPrice: number;
  expectedDeliveryDate: Date;
}
