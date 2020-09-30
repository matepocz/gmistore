import {ProductTableModel} from "./productTableModel";

export interface PagedSellerProductListModel{
  products: Array<ProductTableModel>;
  totalElements: number;
}
