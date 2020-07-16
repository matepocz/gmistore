import { Component } from '@angular/core';
import {UserService} from "./service/user.service";
import {User} from "./models/user";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'gmistore';


  constructor(private userService:UserService) {
  this.userService.getUsers()
    .subscribe((resp:User[])=>{
      console.log(resp);
    });
  }


}
