import {ProductModel} from "../product-model";

export interface OrderModel {
  id: number;
  generatedUniqueId: string;
  status: string;
  quantity: string;
  orderedAt: Date;
  deliveryDate: any;
  productList: Array<ProductModel>;
  user: object;
}
