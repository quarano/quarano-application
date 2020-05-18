import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import * as jwt_decode from 'jwt-decode';
import { distinctUntilChanged, filter } from 'rxjs/operators';
import { SnackbarService } from './snackbar.service';
import { Router } from '@angular/router';

export const JWT_STORAGE_KEY = 'jwt';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  private readonly token$$: BehaviorSubject<string>;
  public readonly token$: Observable<string>;

  public readonly roles$$ = new BehaviorSubject<string[]>([]);
  private readonly username$$ = new BehaviorSubject<string>(null);
  private readonly expDateTime$$ = new BehaviorSubject<Date>(null);

  public get token(): string | null {
    if (this.expDateTime$$.getValue() !== null && this.expDateTime$$.getValue() < new Date()) {
      this.unsetToken();
    }
    return this.token$$.getValue();
  }

  private get localJwt(): string | null {
    const storageToken = localStorage[JWT_STORAGE_KEY];

    if (!storageToken) {
      return null;
    }

    return storageToken;
  }

  private set localJwt(token) {
    if (!token) {
      localStorage.removeItem(JWT_STORAGE_KEY);
    } else {
      localStorage[JWT_STORAGE_KEY] = token;
    }
  }

  constructor(
    private snackbarService: SnackbarService,
    private router: Router) {
    const storageToken = this.localJwt;
    this.token$$ = new BehaviorSubject<string>(storageToken);
    this.token$ = this.token$$.asObservable();

    // Subscribe to token change
    this.token$.subscribe(token => {
      if (token !== null) {
        const payload = jwt_decode(token);
        this.roles$$.next(payload.aut);
        this.username$$.next(payload.sub);
        this.expDateTime$$.next(new Date(payload.exp * 1000));
        this.localJwt = token;
      } else {
        this.roles$$.next(null);
        this.username$$.next(null);
        this.expDateTime$$.next(null);
        this.localJwt = null;
      }

      // Subscribe to expired Token
      this.expDateTime$$
        .pipe(
          distinctUntilChanged(),
          filter(expDate => expDate !== null),
          filter(expDate => expDate <= new Date())
        )
        .subscribe(() => {
          this.snackbarService.message('Ihre Sitzung ist abgelaufen. Bitte melden Sie sich erneut an.');
          this.unsetToken();
        });

    });
  }

  public setToken(token: string) {
    this.token$$.next(token);
  }

  public unsetToken() {
    this.token$$.next(null);
    this.router.navigate(['/welcome/login']);
  }

  public isAuthenticated() {
    return this.token$$.getValue() !== null;
  }
}
