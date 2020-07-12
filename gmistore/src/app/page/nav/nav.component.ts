import {Component, OnInit} from '@angular/core';
import {ConfigService} from "../../service/config.service";
import {AuthService} from "../../auth/auth.service";


@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit {

  // title = 'gmistore';
  public isCollapsed=true;

  constructor(private config: ConfigService,public authService: AuthService) {
  }

  ngOnInit(): void {
  }

  logout() {
    // @ts-ignore
    this.authService.logout();
  }
}
