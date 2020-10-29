import { Store, select } from '@ngrx/store';
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LanguageSelectors } from '../store/selector-types';
import { switchMap, take } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class LanguageInterceptor implements HttpInterceptor {
  constructor(private store: Store) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!request.url.startsWith('./')) {
      return this.store.pipe(
        select(LanguageSelectors.selectedLanguage),
        take(1),
        switchMap((selectedLanguage) => {
          return next.handle(request.clone({ setHeaders: { 'Accept-Language': selectedLanguage?.key || 'de' } }));
        })
      );
    }
    return next.handle(request.clone());
  }
}
