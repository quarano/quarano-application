import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, CanLoad, Route, Router, RouterStateSnapshot, UrlSegment, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {UserService} from '../services/user.service';
import {TokenService} from '../services/token.service';

@Injectable({
  providedIn: 'root'
})
export class IsNotAuthenticatedGuard implements CanActivate, CanLoad {

  constructor(private tokenService: TokenService,
              private userService: UserService,
              private router: Router) {
  }

  canLoad(route: Route, segments: UrlSegment[]): Observable<boolean> | Promise<boolean> | boolean {
    if (!this.tokenService.isAuthenticated()) {
      return true;
    }

    if (this.userService.isHealthDepartmentUser()) {
      this.router.navigate(['/tenant-admin']);
    } else {
      this.router.navigate(['/diary']);
    }
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (!this.tokenService.isAuthenticated()) {
      return true;
    }

    if (this.userService.isHealthDepartmentUser()) {
      this.router.navigate(['/tenant-admin']);
    } else {
      this.router.navigate(['/diary']);
    }
  }
}
