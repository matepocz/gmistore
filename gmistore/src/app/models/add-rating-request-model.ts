export interface AddRatingRequestModel {
  product: string;
  actualRating: number;
  title: string;
  positiveComment: string;
  negativeComment: string;
  pictures: Array<string>;
}
