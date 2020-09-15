import {ProductCategoryModel} from "./product-category.model";

export interface MainCategoryModel {
  id: number;
  key: string;
  displayName: string;
  icon: string;
  subCategories: Array<ProductCategoryModel>;
}
