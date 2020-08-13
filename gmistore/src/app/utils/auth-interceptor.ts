import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {LocalStorageService} from "ngx-webstorage";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private localStorageService: LocalStorageService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.localStorageService.retrieve('authenticationToken')
    if (!token) {
      req = req.clone({
        withCredentials: true
      })
      return next.handle(req);
    }

    const req1 = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`),
      withCredentials: true
    });

    return next.handle(req1);
  }

}
