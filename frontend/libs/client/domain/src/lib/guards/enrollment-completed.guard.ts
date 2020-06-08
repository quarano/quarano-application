import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { UserService } from '@qro/auth/api';
import { EnrollmentService } from '../services/enrollment.service';
import { SnackbarService } from '@qro/shared/util';

@Injectable({
  providedIn: 'root',
})
export class EnrollmentCompletedGuard implements CanActivate {
  constructor(
    private enrollmentService: EnrollmentService,
    private userService: UserService,
    private router: Router,
    private snackbarService: SnackbarService
  ) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.userService.isHealthDepartmentUser) {
      this.router.navigate(['/auth/forbidden']);
      return false;
    }

    return this.enrollmentService.getEnrollmentStatus().pipe(
      map((status) => {
        if (status?.complete) {
          return true;
        }
        this.snackbarService.message('Bitte schließen Sie zunächst die Registrierung ab');
        this.router.navigate(['/client/enrollment/basic-data']);
        return false;
      })
    );
  }
}
