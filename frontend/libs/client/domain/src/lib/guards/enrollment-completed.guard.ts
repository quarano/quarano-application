import { ClientStore } from './../store/client-store.service';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { Observable, of } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { UserService } from '@qro/auth/api';
import { SnackbarService } from '@qro/shared/util-snackbar';

@Injectable({
  providedIn: 'root',
})
export class EnrollmentCompletedGuard implements CanActivate {
  constructor(
    private clientStore: ClientStore,
    private userService: UserService,
    private router: Router,
    private snackbarService: SnackbarService
  ) {}

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    if (this.userService.isHealthDepartmentUser) {
      this.router.navigate(['/auth/forbidden']);
      return of(false);
    }
    return this.clientStore.getEnrollmentStatus().pipe(
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
