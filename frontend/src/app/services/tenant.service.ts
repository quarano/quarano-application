import {Inject, Injectable} from '@angular/core';
import {DOCUMENT} from '@angular/common';
import {TenantsEnum} from './tenantsEnum';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {distinctUntilChanged, filter, map, skip, tap} from 'rxjs/operators';
import {Tenant} from '../models/tenant';
import {Router} from '@angular/router';
import {SnackbarService} from './snackbar.service';

const MOCK_TENANT_1: Tenant = {
  tenantId: 'Testamt1',
  name: 'Gesundheitsamt Testhausen',
  userName: 'testhausen'
};
const MOCK_TENANT_2: Tenant = {
  tenantId: 'Testamt2',
  name: 'Gesundheitsamt Testberg',
  userName: 'testberg'
};

@Injectable({
  providedIn: 'root'
})
export class TenantService {

  public readonly urlTenant: TenantsEnum;
  public readonly tenant$$ = new BehaviorSubject<Tenant>(null);

  constructor(@Inject(DOCUMENT) private document,
              private router: Router,
              private snackbarService: SnackbarService) {
    this.getTenantFromLocation(document.location);

    // Navigate on logout and notify user
    this.tenant$$.asObservable()
      .pipe(
        skip(1),
        distinctUntilChanged(),
        filter((tenant) => tenant === null),
        tap(() => this.snackbarService.message('Sie wurden abgemeldet'))
      )
      .subscribe(() => this.router.navigate(['/tenant-admin/login']));
  }

  private getTenantFromLocation(location: Location) {
    const hostname = location.hostname;

    const domainParts = hostname.split('.');
    const subdomain = domainParts[0];

    switch (subdomain) {
      case 'develop': {
        return TenantsEnum.develop;
      }
      case 'testhausen': {
        return TenantsEnum.testhausen;
      }
      case 'testberg': {
        return TenantsEnum.testberg;
      }
      default: {
        return TenantsEnum.default;
      }
    }
  }

  public logout() {
    this.tenant$$.next(null);
  }

  public checkLogin(username: string, password: string): Observable<Tenant> {
    if (username !== MOCK_TENANT_1.userName && username !== MOCK_TENANT_2.userName) {
      return of(MOCK_TENANT_2).pipe(map(val => {
        throw new Error('invalid username');
      }));
    }

    const mockTenant = username === MOCK_TENANT_1.userName ? MOCK_TENANT_1 : MOCK_TENANT_2;


    return of(mockTenant)
      .pipe(
        map(tenant => {
          // Validate correct login for mock
          if (username === tenant.userName && password === 'test123') {
            return tenant;
          }
          throw new Error('Incorrect username or password');
        }),
        tap(tenant => this.tenant$$.next(tenant))
      );
  }
}
