import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {MessagesResponseModel} from "../models/messages/messages-response.model";
import {NewMessageRequestModel} from "../models/messages/new-message-request.model";

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  private messagesApi = environment.apiUrl + 'api/messages';

  constructor(private httpClient: HttpClient) {
  }

  createMessage(message: NewMessageRequestModel): Observable<boolean> {
    return this.httpClient.post<boolean>(this.messagesApi, message);
  }

  getCountOfUnreadMails(): Observable<number> {
    return this.httpClient.get<number>(this.messagesApi + '/unread-mail-count');
  }

  getMessages(): Observable<MessagesResponseModel> {
    return this.httpClient.get<MessagesResponseModel>(this.messagesApi);
  }

  markMessageRead(id: number): Observable<boolean> {
    return this.httpClient.put<boolean>(this.messagesApi + '/mark-read/' + id, {});
  }

  deleteMessage(id: number): Observable<boolean> {
    return this.httpClient.delete<boolean>(this.messagesApi + '/' + id);
  }
}
