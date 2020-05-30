import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from '../../../../../../apps/quarano-frontend/src/app/services/api.service';
import { distinctUntilChanged, map, tap } from 'rxjs/operators';
import { SnackbarService } from '@qro/shared/util';
import { TokenService } from './token.service';
import { UserDto } from '../../../../../../apps/quarano-frontend/src/app/models/user';
import { HealthDepartmentDto } from '../../../../../../apps/quarano-frontend/src/app/models/healthDepartment';
import { roles } from '../models/role';
import { AuthService } from './auth.service';


@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(
    private apiService: ApiService,
    private authService: AuthService,
    private snackbarService: SnackbarService,
    private tokenService: TokenService) {
  }

  public get user$(): Observable<UserDto> {
    return this.apiService.getMe()
      .pipe(distinctUntilChanged());
  }

  public get client$() {
    return this.user$.pipe(
      distinctUntilChanged(),
      map(user => user?.client));
  }

  public get healthDepartment$(): Observable<HealthDepartmentDto> {
    return this.user$
      .pipe(distinctUntilChanged(),
        map(user => user?.healthDepartment));
  }

  public get currentUserName$(): Observable<string> {
    return this.user$.pipe(
      map(user => {
        if (user) {
          if (this.isHealthDepartmentUser) {
            if (user.firstName && user.lastName) {
              return `${user.firstName} ${user.lastName} (${user.healthDepartment?.name || 'Gesundheitsamt unbekannt'})`;
            }
            return `${user.username} (${user.healthDepartment?.name || 'Gesundheitsamt unbekannt'})`;
          } else if (user.client?.firstName || user.client?.lastName) {
            return `${user.client.firstName || ''} ${user.client.lastName || ''}`;
          }
          return user.username;
        }
        return null;
      })
    );
  }

  public get isLoggedIn$(): Observable<boolean> {
    return this.tokenService.token$
      .pipe(
        distinctUntilChanged(),
        map(token => token !== null)
      );
  }

  public login(username: string, password: string): Observable<any> {
    return this.authService.login(username, password)
      .pipe(
        tap(response => this.tokenService.setToken(response.token)));
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
    return currentRoles.filter(value => roleNames.includes(value)).length > 0;
  }

  public get isHealthDepartmentUser(): boolean {
    return this.roleMatch(roles.filter(r => r.isHealthDepartmentUser).map(r => r.name));
  }

  public get isAdmin(): boolean {
    return this.roleMatch(roles.filter(r => r.isAdmin).map(r => r.name));
  }
}
