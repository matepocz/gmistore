import {Product} from "../product/product";

export interface OrderModel {
  id: number;
  generatedUniqueId: number;
  status: string;
  quantity: string;
  date: any;
  deliveryDate:any;
  productList:Array<Product>;
  user:object;
}
