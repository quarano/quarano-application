import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';
import { TokenService } from '@services/token.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private tokenService: TokenService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler) {
    const authToken = this.tokenService.token;

    if (authToken) {
      const authReq = request.clone({ setHeaders: { Authorization: `Bearer ${authToken}` } });
      return next.handle(authReq);
    }

    return next.handle(request.clone());
  }
}
