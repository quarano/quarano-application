import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { UserService } from '@qro/auth/api';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class IsAdminGuard implements CanActivate {
  constructor(private userService: UserService, private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.userService.isAdmin) {
      return true;
    }

    return this.userService.isLoggedIn$.pipe(
      map((loggedIn) => {
        if (loggedIn) {
          this.router.navigate(['/auth/forbidden']);
        }

        return false;
      })
    );
  }
}
