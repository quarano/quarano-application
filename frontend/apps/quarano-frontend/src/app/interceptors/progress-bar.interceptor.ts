import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';
import { tap, finalize } from 'rxjs/operators';
import { ProgressBarService } from '../services/progress-bar.service';

@Injectable()
export class ProgressBarInterceptor implements HttpInterceptor {

  constructor(private progressBarService: ProgressBarService) {

  }

  intercept(request: HttpRequest<any>, next: HttpHandler) {

    return next.handle(request).pipe(
      tap(() => {
        this.progressBarService.progressBarState = true;
      }),
      finalize(() => {
        this.progressBarService.progressBarState = false;
      }));
  }
}
