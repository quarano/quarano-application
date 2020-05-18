
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import {UserService} from '../services/user.service';
import {EnrollmentService} from '../services/enrollment.service';
import {SnackbarService} from '../services/snackbar.service';

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

    if (this.userService.isHealthDepartmentUser) {
      this.router.navigate(['/forbidden']);
      return false;
    }

    return this.enrollmentService.getEnrollmentStatus()
      .pipe(map(status => {
        if (status?.complete) { return true; }
        this.snackbarService.message('Bitte schließen Sie zunächst die Registrierung ab');
        this.router.navigate(['/basic-data']);
        return false;
      }));
  }
}
