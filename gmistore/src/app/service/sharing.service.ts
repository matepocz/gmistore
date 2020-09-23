import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {UserModel} from "../models/user/user-model";

@Injectable({
  providedIn: 'root'
})
export class SharingService {

  private message: BehaviorSubject<UserModel> = new BehaviorSubject<UserModel>(null);
  sharedMessage = this.message.asObservable();

  constructor() { }

  nextMessage(message: UserModel) {
    this.message.next(message)
  }
}
