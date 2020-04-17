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

    if (this.userService.isHealthDepartmentUser) { return true; }

    return this.userService.enrollmentCompleted$
      .pipe(map(isFullyAuthenticated => {
        console.log(isFullyAuthenticated);
        if (isFullyAuthenticated) { return true; }
        this.snackbarService.message('Bitte schließen Sie zunächst die Registrierung ab');
        this.router.navigate(['/basic-data']);
      }));
  }
}
