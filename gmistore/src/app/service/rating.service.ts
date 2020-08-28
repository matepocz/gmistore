import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {RatingModel} from "../models/rating-model";
import {environment} from "../../environments/environment";
import {RatingInitDataModel} from "../models/rating-init-data-model";
import {AddRatingRequestModel} from "../models/add-rating-request-model";

@Injectable({
  providedIn: 'root'
})
export class RatingService {

  ratingUrl = environment.apiUrl + 'api/ratings';

  constructor(private httpClient: HttpClient) {
  }

  getRatingsByProductSlug(slug: string): Observable<Array<RatingModel>> {
    return this.httpClient.get<Array<RatingModel>>(this.ratingUrl + '/' + slug);
  }

  getInitData(slug: string): Observable<RatingInitDataModel> {
    return this.httpClient.get<RatingInitDataModel>(this.ratingUrl + '/init-data/' + slug);
  }

  addNewRating(data: AddRatingRequestModel): Observable<any> {
    return this.httpClient.post(this.ratingUrl, data);
  }

  reportRating(id: number): Observable<boolean> {
    return this.httpClient.put<boolean>(this.ratingUrl + '/report/' + id, {});
  }

  removeRating(id: number): Observable<boolean> {
    return this.httpClient.delete<boolean>(this.ratingUrl + '/' + id);
  }

  upVoteRating(id: number): Observable<any> {
    return this.httpClient.put(this.ratingUrl + '/up-vote-rating/' + id, {});
  }

  removeUpVoteRating(id: number): Observable<any> {
    return this.httpClient.delete(this.ratingUrl + '/up-vote-rating/' + id);
  }
}
