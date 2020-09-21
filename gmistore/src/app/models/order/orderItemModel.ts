import {ProductModel} from "../product-model";

export interface OrderItemModel {
  id: number;
  product: ProductModel;
  quantity: number;
  price: number;
}
