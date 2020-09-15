export interface NewCategoryModel {
  key: string;
  displayName: string;
  icon: string;
  isActive: boolean;
  mainCategoryKey?: string;
  isSubCategory: boolean;
}
