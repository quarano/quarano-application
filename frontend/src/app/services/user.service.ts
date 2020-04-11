import { EnrollmentService } from './enrollment.service';
import { ClientStatus } from './../models/client-status';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ApiService } from './api.service';
import { distinctUntilChanged, filter, map, switchMap, tap } from 'rxjs/operators';
import { SnackbarService } from './snackbar.service';
import { TokenService } from './token.service';
import { HealthDepartmentDto } from '../models/healtDepartment';
import { User } from '../models/user';

export const HEALTH_DEPARTMENT_ROLES = ['ROLE_HD_ADMIN', 'ROLE_HD_CASE_AGENT'];

@Injectable({
  providedIn: 'root'
})
export class UserService {
  public readonly user$$ = new BehaviorSubject<User>(null);
  private readonly clientStatus$$ = new BehaviorSubject<ClientStatus>(null);
  public readonly clientStatus$ = this.clientStatus$$.asObservable();
  public readonly client$ = this.user$$.asObservable().pipe(map(user => user?.client));
  public readonly roles$$ = this.tokenService.roles$$;
  public readonly healthDepartment$: Observable<HealthDepartmentDto> = this.user$$.pipe(map(user => user?.healthdepartment));

  public readonly isLoggedIn$: Observable<boolean>;
  public readonly isFullyAuthenticated$: Observable<boolean>;
  public readonly isHealthDepartmentUser$: Observable<boolean>;

  public readonly completedPersonalData$: Observable<boolean>;
  public readonly completedQuestionnaire$: Observable<boolean>;
  public readonly completedContactRetro$: Observable<boolean>;

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

    this.completedPersonalData$ = this.clientStatus$.pipe(distinctUntilChanged(), map(status => status?.completedPersonalData));
    this.completedQuestionnaire$ = this.clientStatus$.pipe(distinctUntilChanged(), map(status => status?.completedQuestionnaire));
    this.completedContactRetro$ = this.clientStatus$.pipe(distinctUntilChanged(), map(status => status?.completedContactRetro));

    this.isHealthDepartmentUser$ = this.roles$$
      .pipe(
        distinctUntilChanged(),
        map(roles => this.isHealthDepartmentUser(roles))
      );
  }

  private init() {
    // Check for client, if there is a new token
    this.tokenService.token$.pipe(
      filter(token => token !== null),
      switchMap(() => this.apiService.getMe())
    ).subscribe(user => this.user$$.next(user));

    this.tokenService.token$.pipe(
      filter(token => token !== null),
      switchMap(() => this.enrollmentService.getEnrollmentStatus())
    ).subscribe(status => this.clientStatus$$.next(status));

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
