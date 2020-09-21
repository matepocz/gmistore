import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {EmailModel} from "../models/email-model";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";


@Injectable({
  providedIn: 'root'
})
export class EmailSendingService {
  private baseURL = environment.apiUrl + 'api/contact-us'

  constructor(private http: HttpClient) {

  }

  saveEmailFromUser(data: EmailModel): Observable<any> {
    return this.http.post(this.baseURL, data);
  }
}
