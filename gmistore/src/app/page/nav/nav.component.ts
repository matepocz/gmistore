import {Component, OnInit} from '@angular/core';
import {ConfigService} from "../../service/config.service";
import {AuthService} from "../../auth/auth.service";

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit {
  navigation = this.config.navigation;

  constructor(private config: ConfigService) {
  }

  ngOnInit(): void {
  }

}
