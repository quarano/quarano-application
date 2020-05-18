import { ProgressBarService } from '@services/progress-bar.service';
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';
import { tap, catchError, finalize } from 'rxjs/operators';

@Injectable()
export class ProgressBarInterceptor implements HttpInterceptor {

  constructor(private progressBarService: ProgressBarService) {

  }

  intercept(request: HttpRequest<any>, next: HttpHandler) {

    return next.handle(request).pipe(
      tap(res => {
        this.progressBarService.progressBarState = true;
      }),
      finalize(() => {
        this.progressBarService.progressBarState = false;
      }),
      catchError(error => {
        this.progressBarService.progressBarState = false;
        throw error;
      }));
  }
}
