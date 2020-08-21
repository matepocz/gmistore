import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {RatingModel} from "../models/rating-model";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class RatingService {

  ratingUrl = environment.apiUrl + 'api/ratings';

  constructor(private httpClient: HttpClient) { }

  getRatingsByProductSlug(slug: string): Observable<Array<RatingModel>> {
    return this.httpClient.get<Array<RatingModel>>(this.ratingUrl + '/' + slug);
  }
}
