import { EnrollmentService } from '@services/enrollment.service';
import { SnackbarService } from './../services/snackbar.service';
import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { UserService } from '@services/user.service';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class BasicDataGuard implements CanActivate {

  constructor(
    private enrollmentService: EnrollmentService,
    private userService: UserService,
    private router: Router,
    private snackbarService: SnackbarService) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (this.userService.isHealthDepartmentUser) {
      return false;
    }

    return this.enrollmentService.getEnrollmentStatus()
      .pipe(map(status => {
        if (status?.complete) {
          this.snackbarService.message('Sie haben die Registrierung abgeschlossen');
          this.router.navigate(['/diary']);
          return false;
        }

        return true;
      }));
  }
}
