import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {AuthService} from "../service/auth-service";
import {RoleModel} from "../models/role.model";

@Injectable(
  {providedIn: 'root'}
)
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private authService: AuthService) {
  }

  async canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    let currentUserRoles: Array<RoleModel> = null;

    if (route.data.roles) {
      await this.authService.getUserRoles().then(response => currentUserRoles = response);

      if (currentUserRoles && currentUserRoles.length > 0) {
        for (let i = 0; i < currentUserRoles.length; i++) {
          let role = currentUserRoles[i];
          if (route.data.roles.indexOf(role) !== -1) {
            return true;
          }
        }

        await this.router.navigate(['/']);
        return false;

      } else {
        await this.router.navigate(['/login'], {queryParams: {returnUrl: state.url}});
        return false;
      }
    }
  }
}
