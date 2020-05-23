import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpErrorResponse,
  HTTP_INTERCEPTORS,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { SnackbarService } from '../services/snackbar.service';

export enum HttpStatusCode {
  unauthorized = 401,
  forbidden = 403,
  notFound = 404,
  badRequest = 400,
  internalServerError = 500,
}

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(
    private snackbarService: SnackbarService,
    private router: Router) { }

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler,
  ): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError(error => {
        if (error instanceof HttpErrorResponse) {
          if (error.status === HttpStatusCode.unauthorized.valueOf()) {
            this.snackbarService.message('Sie müssen sich zunächst einloggen');
            this.router.navigate(['/welcome/login']);
            return throwError(error);
          }

          if (error.status === HttpStatusCode.forbidden.valueOf()) {
            this.router.navigate(['/forbidden']);
            return throwError(error);
          }

          if (error.status === HttpStatusCode.internalServerError.valueOf()) {
            // ToDo: Message ggf. anpassen, wenn wir ein Ticketsystem oder eine Supporthotline haben
            this.snackbarService.error('Server Fehler - Für weitere Informationen siehe Konsole');
            console.error(error);
            return throwError(error);
          }

          const applicationError = error.headers.get('Application-Error');
          if (applicationError) {
            console.error(applicationError);
            return throwError(applicationError);
          }

          if (error.status === 0) {
            this.snackbarService.error('Es konnte keine Verbindung zur Api hergestellt werden. Bitte versuchen Sie es später noch einmal.');
            return throwError(error);
          }

          const serverError = error.error;

          if (
            error.status === HttpStatusCode.notFound.valueOf() ||
            (error.status === HttpStatusCode.badRequest.valueOf() &&
              req.method === 'GET')
          ) {
            if (serverError.errors) {
              for (const key in serverError.errors) {
                if (serverError.errors[key]) {
                  this.router.navigate(['/404', serverError.errors[key]]);
                  break;
                }
              }
            } else {
              this.router.navigate(['/404']);
            }
          }

          if (
            error.status === HttpStatusCode.badRequest.valueOf() &&
            (req.method === 'POST' || req.method === 'PUT' || req.method === 'DELETE')
          ) {
            // ToDo ggf. anpassen, wenn das Fehlerformat für 400 vom Backend vorliegt https://jira.quarano.de/browse/CORE-85
            this.snackbarService.error('Die Aktion wurde wegen ungültiger Werte vom Server abgelehnt.');
            console.log(serverError);
          }

          return throwError(serverError || 'Server Fehler');
        }
        return throwError(error);
      }),
    );
  }
}

export const ErrorInterceptorProvider = {
  provide: HTTP_INTERCEPTORS,
  useClass: ErrorInterceptor,
  multi: true,
};
