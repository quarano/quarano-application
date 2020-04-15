import { EnrollmentService } from './enrollment.service';
import { ClientStatusDto } from './../models/client-status';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, combineLatest } from 'rxjs';
import { ApiService } from './api.service';
import { distinctUntilChanged, filter, map, tap, mergeMap } from 'rxjs/operators';
import { SnackbarService } from './snackbar.service';
import { TokenService } from './token.service';
import { HealthDepartmentDto } from '../models/healthDepartment';
import { UserDto } from '../models/user';

export const HEALTH_DEPARTMENT_ROLES = ['ROLE_HD_ADMIN', 'ROLE_HD_CASE_AGENT'];

@Injectable({
  providedIn: 'root'
})
export class UserService {
  public readonly user$$ = new BehaviorSubject<UserDto>(null);
  private readonly clientStatus$$ = new BehaviorSubject<ClientStatusDto>(null);
  public readonly clientStatus$ = this.clientStatus$$.asObservable();
  public readonly client$ = this.user$$.asObservable().pipe(map(user => user?.client));
  public readonly roles$$ = this.tokenService.roles$$;
  public readonly healthDepartment$: Observable<HealthDepartmentDto> = this.user$$.pipe(map(user => user?.healthDepartment));
  public readonly currentUserName$: Observable<string>;

  public readonly isLoggedIn$: Observable<boolean>;
  public readonly isFullyAuthenticated$: Observable<boolean>;
  public readonly isHealthDepartmentUser$: Observable<boolean>;
  public readonly completedEnrollment$: Observable<boolean>;

  constructor(
    private apiService: ApiService,
    private snackbarService: SnackbarService,
    private tokenService: TokenService,
    private enrollmentService: EnrollmentService) {
    this.init();

    this.isLoggedIn$ = this.tokenService.token$
      .pipe(
        map(token => token !== null)
      );

    this.isFullyAuthenticated$ = this.clientStatus$
      .pipe(
        distinctUntilChanged(),
        map(status => status?.complete)
      );

    this.completedEnrollment$ = this.clientStatus$.pipe(distinctUntilChanged(), map(status => status?.complete));

    this.isHealthDepartmentUser$ = this.roles$$
      .pipe(
        distinctUntilChanged(),
        map(roles => this.isHealthDepartmentUser(roles))
      );

    this.currentUserName$ = combineLatest([this.user$$, this.isHealthDepartmentUser$]).pipe(
      map(([user, isHealthDepartmentUser]) => ({ user, isHealthDepartmentUser })),
      map(value => {
        if (value.user) {
          if (value.isHealthDepartmentUser) {
            return `${value.user.username} (${value.user.healthDepartment?.fullName || 'Gesundheitsamt unbekannt'})`;
          } else if (value.user.client?.firstName || value.user.client?.lastName) {
            return `${value.user.client.firstName || ''} ${value.user.client.lastName || ''}`;
          }
          return value.user.username;
        }
        return null;
      })
    );
  }

  private init() {
    // Check for client, if there is a new token
    this.tokenService.token$.pipe(
      filter(token => token !== null),
      mergeMap(() => {
        return this.apiService.getMe()
          .pipe(
            tap(returnedUser => this.user$$.next(returnedUser)),
            mergeMap(returnedUser => returnedUser.client ? this.enrollmentService.getEnrollmentStatus() : null));
      }),
    ).subscribe(status => {
      this.clientStatus$$.next(status);
    });

    // Unset client if token gets null
    this.tokenService.token$.pipe(
      filter(token => token === null)
    ).subscribe(() => {
      this.user$$.next(null);
      this.clientStatus$$.next(null);
    });
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

  public isHealthDepartmentUser(userRoles: string[] = this.roles$$.getValue()): boolean {
    if (!userRoles) {
      return false;
    }

    for (const role of HEALTH_DEPARTMENT_ROLES) {
      if (userRoles.includes(role)) {
        return true;
      }
    }
    return false;
  }
}
