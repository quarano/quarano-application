import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { combineLatest, Observable } from 'rxjs';
import { UserService } from '../services/user.service';
import { distinctUntilChanged, filter, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class IsAuthenticatedFullyClientGuard implements CanActivate {

  constructor(
    private userService: UserService,
    private router: Router) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    return combineLatest([
      this.userService.completedEnrollment$,
      this.userService.isHealthDepartmentUser$])
      .pipe(
        distinctUntilChanged(),
        tap(completed => {
          if (completed[1]) {
            return;
          }
          if (!completed[0]) {
            this.router.navigate(['/basic-data']);
          }
        }),
        map(completed => completed[0]),
        filter(completed => completed)
      );

  }

}
