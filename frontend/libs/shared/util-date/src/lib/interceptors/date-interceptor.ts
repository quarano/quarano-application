import { cloneDeep } from 'lodash';
import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import * as moment from 'moment';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class DateInterceptor implements HttpInterceptor {
  // Migrated from AngularJS https://raw.githubusercontent.com/Ins87/angular-date-interceptor/master/src/angular-date-interceptor.js
  iso8601 = /^\d{4}-\d\d-\d\dT\d\d:\d\d:\d\d(\.\d+)?(([+-]\d\d:\d\d)|Z)?$/;

  intercept(request: HttpRequest<any>, next: HttpHandler) {
    return next.handle(request.clone({ body: this.recursiveTransform(cloneDeep(request.body)) })).pipe(
      tap((event: HttpEvent<any>) => {
        if (event instanceof HttpResponse) {
          const body = event.body;
          this.convertToDate(body);
        }
      })
    );
  }

  recursiveTransform(object) {
    if (object instanceof Date) {
      return moment(object).format(moment.HTML5_FMT.DATETIME_LOCAL_SECONDS);
    }

    if (moment.isMoment(object)) {
      return object.format(moment.HTML5_FMT.DATETIME_LOCAL_SECONDS);
    }

    if (Array.isArray(object)) {
      return object.map((single) => this.recursiveTransform(single));
    }

    if (typeof object === 'object' && object !== null) {
      Object.keys(object).forEach((key) => (object[key] = this.recursiveTransform(object[key])));
      return object;
    }

    return object;
  }

  convertToDate(body) {
    if (body === null || body === undefined) {
      return body;
    }

    if (typeof body !== 'object') {
      return body;
    }

    for (const key of Object.keys(body)) {
      const value = body[key];
      if (this.isIso8601(value)) {
        body[key] = new Date(value);
      } else if (typeof value === 'object') {
        this.convertToDate(value);
      }
    }
  }

  isIso8601(value) {
    if (value === null || value === undefined) {
      return false;
    }

    return this.iso8601.test(value);
  }
}
