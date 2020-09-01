import { ClientStore } from './../store/client-store.service';
import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { UserService } from '@qro/auth/api';
import { SnackbarService } from '@qro/shared/util-snackbar';

@Injectable({
  providedIn: 'root',
})
export class BasicDataGuard implements CanActivate {
  constructor(
    private clientStore: ClientStore,
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
    return this.clientStore.getEnrollmentStatus().pipe(
      map((status) => {
        if (status?.complete) {
          this.snackbarService.message('Sie haben die Registrierung bereits abgeschlossen');
          this.router.navigate(['/client/diary/diary-list']);
          return false;
        }

        return true;
      })
    );
  }
}
