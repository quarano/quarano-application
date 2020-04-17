import { SnackbarService } from 'src/app/services/snackbar.service';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { UserService } from '../services/user.service';
import { map, switchMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class IsAuthenticatedFullyClientGuard implements CanActivate {

  constructor(
    private userService: UserService,
    private router: Router,
    private snackbarService: SnackbarService) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    return this.userService.isHealthDepartmentUser$
      .pipe(map(isHealthDepartmentUser => {
        if (isHealthDepartmentUser) {
          return true;
        }
      }), switchMap(_ => this.userService.completedEnrollment$
        .pipe(map(completedEnrollment => {
          console.log(completedEnrollment);
          if (completedEnrollment) { return true; }
          this.snackbarService.message('Bitte schließen Sie zunächst die Registrierung ab');
          this.router.navigate(['/basic-data']);
        }))));
  }
}
