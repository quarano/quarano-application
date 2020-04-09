import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {combineLatest, Observable} from 'rxjs';
import {UserService} from '../services/user.service';
import {distinctUntilChanged, filter, map, tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class IsAuthenticatedFullyClientGuard implements CanActivate {

  constructor(private userService: UserService,
              private router: Router) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    return combineLatest([
      this.userService.completedPersonalData$,
      this.userService.completedQuestionnaire$,
      this.userService.completedContactRetro$,
      this.userService.isHealthDepartmentUser$])
      .pipe(
        distinctUntilChanged(),
        tap(completed => {
          if (completed[3]) {
            return;
          }
          if (!completed[0] || !completed[1] || !completed[2]) {
            this.router.navigate(['/basic-data']);
          }
        }),
        map(completed => completed[0] && completed[1] && completed[2]),
        filter(completed => completed)
      );

  }

}
