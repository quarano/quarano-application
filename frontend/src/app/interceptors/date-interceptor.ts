import {Injectable} from '@angular/core';
import {HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import * as moment from 'moment';

@Injectable()
export class DateInterceptor implements HttpInterceptor {

  intercept(request: HttpRequest<any>, next: HttpHandler) {
    return next.handle(request.clone({body: this.recursiveTransform(request.body)}));
  }

  recursiveTransform(object) {
    if (object instanceof Date) {
      return moment(object).format(moment.HTML5_FMT.DATETIME_LOCAL_SECONDS);
    }

    if (Array.isArray(object)) {
      return object.map((single) => this.recursiveTransform(single));
    }

    if (typeof object === 'object' && object !== null) {
      Object.keys(object).forEach((key) => object[key] = this.recursiveTransform(object[key]));

      return object;
    }

    return object;
  }
}
