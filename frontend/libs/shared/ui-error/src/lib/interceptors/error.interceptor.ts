import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpErrorResponse,
  HTTP_INTERCEPTORS,
} from '@angular/common/http';
import { Observable, throwError, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

export enum HttpStatusCode {
  unauthorized = 401,
  forbidden = 403,
  notFound = 404,
  badRequest = 400,
  internalServerError = 500,
  unprocessableEntity = 422,
  preconditionFailed = 412,
}

export interface IErrorToDisplay {
  errors: any;
  status: number;
}

export interface IUnprocessableEntityError {
  unprocessableEntityErrors: any;
}

@Injectable({
  providedIn: 'root',
})
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error) => {
        if (error instanceof HttpErrorResponse) {
          if (error.status === HttpStatusCode.internalServerError.valueOf()) {
            // ToDo: Message ggf. anpassen, wenn wir ein Ticketsystem oder eine Supporthotline haben
            this.router.navigate(['/error'], {
              queryParams: {
                message: encodeURIComponent('ERROR.SERVERFEHLER'),
              },
            });
            return of(null);
          }

          const applicationError = error.headers.get('Application-Error');
          if (applicationError) {
            console.error(applicationError);
            return throwError(applicationError);
          }

          if (error.status === 0) {
            this.router.navigate(['/error'], {
              queryParams: {
                message: encodeURIComponent('ERROR.KEINE_VERBINDUNG_ZUR_API'),
              },
            });
            return of(null);
          }

          const serverError = error.error;
          if (error.status === HttpStatusCode.unauthorized.valueOf()) {
            this.router.routeReuseStrategy.shouldReuseRoute = () => false;
            this.router.onSameUrlNavigation = 'reload';
            this.router.navigate(
              ['/auth/login'],
              serverError
                ? {
                    queryParams: {
                      message: encodeURIComponent(serverError),
                    },
                  }
                : null
            );
            return throwError(error);
          }

          if (error.status === HttpStatusCode.forbidden.valueOf()) {
            this.router.navigate(
              ['/auth/forbidden'],
              serverError
                ? {
                    queryParams: {
                      message: encodeURIComponent(serverError),
                    },
                  }
                : null
            );
          }

          if (
            error.status === HttpStatusCode.notFound.valueOf() ||
            (error.status === HttpStatusCode.badRequest.valueOf() && req.method === 'GET')
          ) {
            if (serverError?.errors) {
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
            (error.status === HttpStatusCode.badRequest.valueOf() &&
              (req.method === 'POST' || req.method === 'PUT' || req.method === 'DELETE')) ||
            error.status === HttpStatusCode.preconditionFailed.valueOf()
          ) {
            return throwError({ errors: serverError, status: error.status } as IErrorToDisplay);
          }

          if (
            error.status === HttpStatusCode.unprocessableEntity.valueOf() &&
            (req.method === 'POST' || req.method === 'PUT' || req.method === 'DELETE')
          ) {
            return throwError({ unprocessableEntityErrors: serverError } as IUnprocessableEntityError);
          }

          return throwError(serverError || 'Server Error');
        }
        return throwError(error);
      })
    );
  }
}

export const ErrorInterceptorProvider = {
  provide: HTTP_INTERCEPTORS,
  useClass: ErrorInterceptor,
  multi: true,
};
