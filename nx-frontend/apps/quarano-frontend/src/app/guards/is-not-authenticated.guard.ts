import {SnackbarService} from './../services/snackbar.service';
import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {TokenService} from '../services/token.service';
import {UserService} from '../services/user.service';

@Injectable({
  providedIn: 'root'
})
export class IsNotAuthenticatedGuard implements CanActivate {

  constructor(
    private tokenService: TokenService,
    private userService: UserService,
    private router: Router,
    private snackbarService: SnackbarService) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (!this.tokenService.isAuthenticated()) {
      return true;
    }

    this.snackbarService.message('Diese Seite ist nicht verfügbar für eingeloggte User');
    if (this.userService.isHealthDepartmentUser) {
      this.router.navigate(['/tenant-admin']);
      return false;
    } else {
      this.router.navigate(['/diary']);
      return false;
    }
  }
}
