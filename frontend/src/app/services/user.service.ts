import { EnrollmentStatusDto } from '@models/enrollment-status';
import { EnrollmentService } from '@services/enrollment.service';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { ApiService } from './api.service';
import { distinctUntilChanged, map, tap, switchMap } from 'rxjs/operators';
import { SnackbarService } from './snackbar.service';
import { TokenService } from './token.service';
import { HealthDepartmentDto } from '@models/healthDepartment';
import { UserDto } from '@models/user';
import { roles } from '@models/role';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(
    private apiService: ApiService,
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
    return this.apiService.login(username, password)
      .pipe(
        tap(response => this.tokenService.setToken(response.token)));
  }

  public logout() {
    this.snackbarService.message('Sie wurden abgemeldet');
    this.tokenService.unsetToken();
  }

  public roleMatch(roleNames: string[]): boolean {
    const currentRoles = this.tokenService.roles$$.value;
    if (!currentRoles) { return false; }
    return currentRoles.filter(value => roleNames.includes(value)).length > 0;
  }

  public get isHealthDepartmentUser(): boolean {
    return this.roleMatch(roles.filter(r => r.isHealthDepartmentUser).map(r => r.name));
  }

  public get isAdmin(): boolean {
    return this.roleMatch(roles.filter(r => r.isAdmin).map(r => r.name));
  }
}
