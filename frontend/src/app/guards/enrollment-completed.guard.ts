import { EnrollmentService } from '@services/enrollment.service';
import { SnackbarService } from '@services/snackbar.service';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { UserService } from '@services/user.service';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class EnrollmentCompletedGuard implements CanActivate {

  constructor(
    private enrollmentService: EnrollmentService,
    private userService: UserService,
    private router: Router,
    private snackbarService: SnackbarService) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (this.userService.isHealthDepartmentUser) { return true; }

    return this.enrollmentService.getEnrollmentStatus()
      .pipe(map(status => {
        if (status?.complete) { return true; }
        this.snackbarService.message('Bitte schließen Sie zunächst die Registrierung ab');
        this.router.navigate(['/basic-data']);
        return false;
      }));
  }
}
