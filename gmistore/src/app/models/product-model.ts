import {ProductCategoryModel} from "./product-category.model";

export interface ProductModel {
  id: number;
  name: string;
  productCode: string;
  slug: string;
  description: string;
  mainCategory: ProductCategoryModel;
  subCategory: ProductCategoryModel;
  pictureUrl: string;
  pictures: Array<string>;
  price: number;
  discount: number;
  warrantyMonths: number;
  quantityAvailable: number;
  quantitySold: number;
  averageRating: number;
  active: boolean;
  addedBy: string;
}
