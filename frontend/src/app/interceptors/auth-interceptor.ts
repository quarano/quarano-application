import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';
import { USERCODE_STORAGE_KEY } from '../services/user.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  intercept(request: HttpRequest<any>, next: HttpHandler) {
    const authToken = localStorage[USERCODE_STORAGE_KEY];

    if (authToken) {
      const authReq = request.clone({ setHeaders: { 'client-code': authToken } });
      return next.handle(authReq);
    } else {
    }

    return next.handle(request.clone());
  }
}
