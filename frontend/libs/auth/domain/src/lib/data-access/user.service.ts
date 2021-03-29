import { TranslateService } from '@ngx-translate/core';
import { Injectable } from '@angular/core';
import { Observable, combineLatest } from 'rxjs';
import { distinctUntilChanged, map, tap, withLatestFrom } from 'rxjs/operators';
import { TranslatedSnackbarService } from '@qro/shared/util-snackbar';
import { TokenService } from './token.service';
import { roles } from '../model/role';
import { AuthService } from './auth.service';
import { AuthStore } from '../store/auth-store.service';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(
    private authService: AuthService,
    private snackbarService: TranslatedSnackbarService,
    private tokenService: TokenService,
    private authStore: AuthStore,
    private translate: TranslateService
  ) {}

  public get isLoggedIn$(): Observable<boolean> {
    return this.tokenService.token$.pipe(
      distinctUntilChanged(),
      map((token) => token !== null)
    );
  }

  public get nameOfCurrentUser$(): Observable<string> {
    return combineLatest([this.authStore.user$, this.translate.get('USER.GESUNDHEITSAMT_UNBEKANNT')]).pipe(
      map(([user, translatedText]) => {
        if (user) {
          if (this.isHealthDepartmentUser) {
            if (user.firstName && user.lastName) {
              return `${user.firstName} ${user.lastName} (${user.healthDepartment?.name || translatedText})`;
            }
            return `${user.username} (${user.healthDepartment?.name || translatedText})`;
          } else if (user.client?.firstName || user.client?.lastName) {
            return `${user.client.firstName || ''} ${user.client.lastName || ''}`;
          }
          return user.username;
        }
        return null;
      })
    );
  }

  public login(username: string, password: string): Observable<any> {
    return this.authService.login(username, password).pipe(
      tap((res) => this.tokenService.setToken(res.headers.get('X-Auth-Token'))),
      tap((res) => this.authStore.login())
    );
  }

  public logout() {
    this.snackbarService.message('USER.SIE_WURDEN_ABGEMELDET');
    this.tokenService.unsetToken();
    this.authStore.logout();
  }

  public roleMatch(roleNames: string[]): boolean {
    const currentRoles = this.tokenService.roles$$.value;
    if (!currentRoles) {
      return false;
    }
    return currentRoles.filter((value) => roleNames.includes(value)).length > 0;
  }

  public get isHealthDepartmentUser(): boolean {
    return this.roleMatch(roles.filter((r) => r.isHealthDepartmentUser).map((r) => r.name));
  }

  public get isAdmin(): boolean {
    return this.roleMatch(roles.filter((r) => r.isAdmin).map((r) => r.name));
  }
}
