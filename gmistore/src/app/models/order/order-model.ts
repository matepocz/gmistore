import {ProductModel} from "../product-model";

export interface OrderModel {
  id: number;
  generatedUniqueId: number;
  status: string;
  quantity: string;
  orderedAt: Date;
  deliveryDate: any;
  productList: Array<ProductModel>;
  user: object;
}
