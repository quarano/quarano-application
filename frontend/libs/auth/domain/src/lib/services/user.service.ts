import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { distinctUntilChanged, map, tap } from 'rxjs/operators';
import { SnackbarService } from '@qro/shared/util';
import { TokenService } from './token.service';
import { UserDto } from '../models/user';
import { roles } from '../models/role';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(
    private authService: AuthService,
    private snackbarService: SnackbarService,
    private tokenService: TokenService
  ) {}

  public get user$(): Observable<UserDto> {
    return this.authService.getMe().pipe(distinctUntilChanged());
  }

  public get isLoggedIn$(): Observable<boolean> {
    return this.tokenService.token$.pipe(
      distinctUntilChanged(),
      map((token) => token !== null)
    );
  }

  public login(username: string, password: string): Observable<any> {
    return this.authService
      .login(username, password)
      .pipe(tap((response) => this.tokenService.setToken(response.token)));
  }

  public logout() {
    this.snackbarService.message('Sie wurden abgemeldet');
    this.tokenService.unsetToken();
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
