export interface NewCategoryModel {
  key: string;
  displayName: string;
  isActive: boolean;
  mainCategoryKey?: string;
  isSubCategory: boolean;
}
