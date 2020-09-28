import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class LiveDataSubjectService {
  data:any = '';
  dataSource = new BehaviorSubject(this.data);

  constructor() { }

  updatedDataSelection(data:any){
    this.dataSource.next(data);
  }

  asObservable(){
    return this.dataSource.asObservable();
  }
}
