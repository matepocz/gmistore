import {ProductModel} from "./product-model";

export interface OrderModel {
  id: number;
  generatedUniqueId: number;
  status: string;
  quantity: string;
  date: any;
  deliveryDate: any;
  productList: Array<ProductModel>;
  user: object;
}
