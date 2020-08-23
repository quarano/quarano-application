import { UserDto } from './../model/user';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Store, select } from '@ngrx/store';
import { AuthSelectors } from './selector-types';
import { AuthActions } from './action-types';

@Injectable()
export class AuthStore {
  private _user$: Observable<UserDto>;

  get user$(): Observable<UserDto> {
    return this._user$;
  }

  constructor(private store: Store<AuthStore>) {
    this._user$ = this.store.pipe(select(AuthSelectors.user));
  }

  public logout() {
    this.store.dispatch(AuthActions.logout());
  }

  public login() {
    this.store.dispatch(AuthActions.login());
  }
}
