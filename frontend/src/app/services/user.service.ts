import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {Client} from '../models/client';
import {ApiService} from './api.service';
import {filter, map, tap} from 'rxjs/operators';
import {Router} from '@angular/router';
import {SnackbarService} from './snackbar.service';
import {TokenService} from './token.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  public readonly client$$ = new BehaviorSubject<Client>(null);
  public readonly roles$$ = this.tokenService.roles$$;
  public readonly username$$ = this.tokenService.username$$;
  public readonly isLoggedIn$: Observable<boolean>;
  public readonly isAuthenticatedFully$$ = new BehaviorSubject<boolean>(null);

  constructor(private apiService: ApiService,
              private snackbarService: SnackbarService,
              private tokenService: TokenService,
              private router: Router) {
    this.init();

    this.isLoggedIn$ = this.tokenService.token$
      .pipe(
        map(token => token !== null)
      );
  }

  private init() {
    // Check for user, if there is a new token
    this.tokenService.token$.pipe(
      filter(token => token !== null)
    ).subscribe();

    // Unset user if token gets null
    this.tokenService.token$.pipe(
      filter(token => token === null)
    ).subscribe(
      () => this.client$$.next(null)
    );
  }

  public login(username: string, password: string): Observable<any> {
    return this.apiService.login(username, password)
      .pipe(
        tap(response => this.tokenService.setToken(response.token))
      );
  }

  public logout() {
    this.snackbarService.message('Sie wurden abgemeldet');
    this.tokenService.unsetToken();
  }

  public hasRole(role: string, rolesList: Array<string> = this.roles$$.getValue()) {
    return rolesList.includes(role);
  }
}
