import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  private messagesApi = environment.apiUrl + 'api/messages';

  constructor(private httpClient: HttpClient) { }

  getCountOfUnreadMails(): Observable<number> {
    return this.httpClient.get<number>(this.messagesApi + '/unread-mail-count');
  }
}
