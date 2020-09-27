import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {EmailModel} from "../models/email-model";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";


@Injectable({
  providedIn: 'root'
})
export class EmailSendingService {
  private baseURL = environment.apiUrl + 'api/contact-messages'

  constructor(private http: HttpClient) {

  }

  saveEmailFromUser(data: EmailModel): Observable<EmailModel> {
    return this.http.post<EmailModel>(this.baseURL, data);
  }


  getIncomeEmails(): Observable<Array<EmailModel>> {
    return this.http.get<Array<EmailModel>>(this.baseURL+'/income-emails');
  }
}
