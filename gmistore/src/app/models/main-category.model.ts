import {ProductCategoryModel} from "./product-category.model";

export interface MainCategoryModel {
  id: number;
  key: string;
  displayName: string;
  subCategories: Array<ProductCategoryModel>;
}
