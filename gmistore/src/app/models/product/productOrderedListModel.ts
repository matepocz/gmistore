export interface ProductOrderedListModel {
  id: number;
  name: string;
  productCode: string;
  features: Array<string>;
  pictureUrl: string;
  pictures: Array<string>;
  price: number;
  discount: number;
  averageRating: number;
  orderItemId: number;
}
