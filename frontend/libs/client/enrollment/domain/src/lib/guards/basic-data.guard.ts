import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { UserService } from '@qro/auth/api';
import { SnackbarService } from '@qro/shared/util';
import { EnrollmentService } from '../services/enrollment.service';

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
      this.router.navigate(['/forbidden']);
      return false;
    }

    return this.enrollmentService.getEnrollmentStatus()
      .pipe(map(status => {
        if (status?.complete) {
          this.snackbarService.message('Sie haben die Registrierung bereits abgeschlossen');
          this.router.navigate(['/diary']);
          return false;
        }

        return true;
      }));
  }
}
