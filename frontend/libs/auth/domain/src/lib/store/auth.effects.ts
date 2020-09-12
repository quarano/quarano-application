import { select, Store } from '@ngrx/store';
import { LanguageActions, LanguageSelectors } from '@qro/shared/util-translation';
import { HttpClient } from '@angular/common/http';
import { Injectable, Inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { switchMap, shareReplay, tap, withLatestFrom } from 'rxjs/operators';
import { API_URL } from '@qro/shared/util-data-access';
import { UserDto } from '../model/user';
import { AuthActions } from './action-types';

@Injectable()
export class AuthEffects {
  constructor(
    private actions$: Actions,
    private httpClient: HttpClient,
    @Inject(API_URL) private apiUrl: string,
    private store: Store
  ) {}

  login$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.login),
      switchMap((action) =>
        this.httpClient
          .get<UserDto>(`${this.apiUrl}/api/user/me`, { observe: 'response' })
          .pipe(shareReplay())
      ),
      withLatestFrom(this.store.pipe(select(LanguageSelectors.supportedLanguages))),
      switchMap(([res, languages]) => {
        const header = res.headers.get('Content-Language') || 'tr';
        console.log('Content Language Header Value:', header);
        if (header) {
          const newLanguage = languages.find((l) => l.key === header);
          if (newLanguage) {
            return [
              AuthActions.userDataLoaded({ user: res.body }),
              LanguageActions.contentLanguageHeaderRead({ selectedLanguage: newLanguage }),
            ];
          } else {
            console.warn(`Language '${header}' supplied by Content-Language header is not supported`);
          }
        }
        return [AuthActions.userDataLoaded({ user: res.body })];
      })
    )
  );

  userDataLoaded$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.userDataLoaded),
        tap((action) => localStorage.setItem('user', JSON.stringify(action.user)))
      ),
    { dispatch: false }
  );

  logout$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.logout),
        tap((action) => localStorage.removeItem('user'))
      ),
    { dispatch: false }
  );
}
