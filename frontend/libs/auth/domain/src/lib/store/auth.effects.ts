import { HttpClient } from '@angular/common/http';
import { Injectable, Inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { map, switchMap, shareReplay, tap } from 'rxjs/operators';
import { API_URL } from '@qro/shared/util-data-access';
import { UserDto } from '../model/user';
import { AuthActions } from './action-types';

@Injectable()
export class AuthEffects {
  constructor(private actions$: Actions, private httpClient: HttpClient, @Inject(API_URL) private apiUrl: string) {}

  login$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.login),
      switchMap((action) => this.httpClient.get<UserDto>(`${this.apiUrl}/api/user/me`).pipe(shareReplay())),
      map((user) => AuthActions.userDataLoaded({ user }))
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
