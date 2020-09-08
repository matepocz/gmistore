import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {AuthService} from "../service/auth-service";

@Injectable(
  {providedIn: 'root'}
)
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private authService: AuthService) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const currentUserRoles = this.authService.getCurrentUserRoles;
    let canActivate = false;
    if (route.data.roles) {
      if (currentUserRoles) {
        currentUserRoles.forEach((role) => {
          if (route.data.roles.indexOf(role) !== -1) {
            canActivate = true;
            return;
          }
        });

        if (canActivate) {
          return true;
        }
        this.router.navigate(['/']);
        return false;
      } else {
        this.router.navigate(['/login'], {queryParams: {returnUrl: state.url}});
        return false;
      }
    }
  }
}
