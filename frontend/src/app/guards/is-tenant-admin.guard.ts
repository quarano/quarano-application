import {Injectable} from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router, CanLoad, Route, UrlSegment} from '@angular/router';
import {Observable} from 'rxjs';
import {TenantService} from '../services/tenant.service';
import {UserService} from '../services/user.service';

@Injectable({
  providedIn: 'root'
})
export class IsTenantAdminGuard implements CanActivate, CanLoad {

  constructor(private userService: UserService,
              private tenantService: TenantService,
              private router: Router) {
  }

  canLoad(route: Route, segments: UrlSegment[]): Observable<boolean> | Promise<boolean> | boolean {
    return this.check();
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.check();
  }

  private check() {
    if (this.userService.hasRole('ROLE_HD_ADMIN') || this.userService.hasRole('ROLE_HD_CASE_AGENT')) {
      return true;
    }

    this.router.navigate(['/welcome/login']);
  }

}
