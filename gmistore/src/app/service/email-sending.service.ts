import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {EmailModel} from "../models/messages/email-model";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {ReplyEmailModel} from "../models/messages/reply-email-model";


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


  getAllActiveIncomeEmails(): Observable<Array<EmailModel>> {
    return this.http.get<Array<EmailModel>>(this.baseURL+'/income-emails');
  }

  sendReplyEmailToUser(data: ReplyEmailModel): Observable<boolean>{
    return  this.http.post<boolean>(this.baseURL+'/reply',data)
  }

  deleteEmail(id: number): Observable<boolean> {
    return this.http.delete<boolean>(this.baseURL+'/'+id);
  }
}
