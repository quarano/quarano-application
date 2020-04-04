import { Injectable, } from '@angular/core';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { Client } from '../models/client';
import { ApiService } from './api.service';
import { catchError, switchMap, tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { FirstQuery } from '../models/first-query';

export const USERCODE_STORAGE_KEY = 'covu';
export const CLIENT_KEY = 'client';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly client$$ = new BehaviorSubject<Client>(null);

  public isAuthenticated$$ = new BehaviorSubject<boolean>(false);

  public get user(): Client | null {
    return JSON.parse(localStorage.getItem(CLIENT_KEY));
  }

  public set user(client: Client | null) {
    if (client === null) {
      localStorage.removeItem(CLIENT_KEY);
    } else {
      localStorage.setItem(CLIENT_KEY, JSON.stringify(client));
    }
  }

  public get localClientCode(): string | null {
    return localStorage[USERCODE_STORAGE_KEY];
  }

  public set localClientCode(code) {
    if (code === null) {
      localStorage.removeItem(USERCODE_STORAGE_KEY);
    } else {
      localStorage[USERCODE_STORAGE_KEY] = code;
    }
  }

  constructor(
    private apiService: ApiService,
    private router: Router) {
    const clientCode = this.localClientCode;
    if (clientCode !== undefined) {
      this.checkCodeGetClient(clientCode).subscribe(
        () => this.router.navigate(['/diary'])
      );
    }
  }

  public isFullyAuthenticated(): boolean {
    const authenticated = this.localClientCode !== null && this.user !== null;
    this.isAuthenticated$$.next(authenticated);
    return authenticated;
  }

  private checkCodeGetClient(code: string, withErrorNavigation = true): Observable<Client> {
    if (code === undefined) {
      return throwError('No code present!');
    }
    return this.apiService.getClientByCode(code)
      .pipe(
        catchError(error => {
          this.localClientCode = null;
          if (withErrorNavigation) {
            // this.router.navigate(['/welcome']);
          }
          return throwError('Code invalid! No Client found.');
        })
      );
  }

  public createClientWithFirstQuery(client: Client, firstQuery: FirstQuery): Observable<Client> {
    let clientCode: string;
    return this.apiService.registerClient(client)
      .pipe(
        tap(clientCodeResponse => clientCode = clientCodeResponse),
        switchMap(clientCodeResponse => this.apiService.createFirstReport(firstQuery, clientCodeResponse)),
        switchMap(() => this.setUserCode(clientCode))
      );
  }

  public setUserCode(code: string): Observable<Client> {
    return this.checkCodeGetClient(code, false)
      .pipe(
        // Get Client
        tap((clientResponse: Client) => {
          this.localClientCode = clientResponse.clientCode;
          this.user = clientResponse;
        })
      );
  }
}
