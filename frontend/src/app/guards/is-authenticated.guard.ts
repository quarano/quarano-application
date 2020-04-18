import { UserService } from '@services/user.service';
import { SnackbarService } from '@services/snackbar.service';
import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class IsAuthenticatedGuard implements CanActivate {

  constructor(
    private userService: UserService,
    private router: Router,
    private snackbarService: SnackbarService) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    return this.userService.isLoggedIn$
      .pipe(map(isLoggedIn => {
        if (isLoggedIn) { return true; }
        this.snackbarService.message('Bitte loggen Sie sich zuerst ein');
        this.router.navigate(['/welcome/login']);
        return false;
      }));
  }
}
