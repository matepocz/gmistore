export class RatingModel {
  id: number;
  active: boolean;
  username: string;
  actualRating: number;
  title: string;
  positiveComment: string;
  negativeComment: string;
  pictures: Array<string>;
  upVotes: number;
  voters: Array<string>;
  timeStamp: Date;
}
