import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { UserService } from '@qro/auth/api';

@Injectable({
  providedIn: 'root',
})
export class IsHealthDepartmentUserGuard implements CanActivate {
  constructor(private userService: UserService, private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.userService.isHealthDepartmentUser) {
      return true;
    }
    this.router.navigate(['/auth/forbidden']);
    return false;
  }
}
