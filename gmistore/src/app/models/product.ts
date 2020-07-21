import {Rating} from "../product/rating";

export interface Product {
  id: number;
  name: string;
  productCode: string;
  slug: string;
  description: string;
  category: string;
  pictureUrl: string;
  pictures: Array<string>;
  price: number;
  discount: number;
  warrantyMonths: number;
  quantityAvailable: number;
  quantitySold: number;
  ratings: Array<Rating>;
  averageRating: number;
  active: boolean;
  addedBy: string;
}
