import { Injectable } from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router, Route, UrlSegment, CanLoad} from '@angular/router';
import { Observable } from 'rxjs';
import {UserService} from '../services/user.service';
import {TokenService} from '../services/token.service';

@Injectable({
  providedIn: 'root'
})
export class IsNotAuthenticatedGuard implements CanActivate, CanLoad {

  constructor(private tokenService: TokenService,
              private router: Router) {
  }

  canLoad(route: Route, segments: UrlSegment[]): Observable<boolean> | Promise<boolean> | boolean {
    if (!this.tokenService.isAuthenticated()) {
      return true;
    }

    this.router.navigate(['/diary']);
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (!this.tokenService.isAuthenticated()) {
      return true;
    }

    this.router.navigate(['/diary']);

  }
}
