import {ProductModel} from "../product-model";

export interface PagedProductListModel {
  products: Array<ProductModel>;
  totalElements: number;
  totalPages: number;
  categoryDisplayName: string;
}
