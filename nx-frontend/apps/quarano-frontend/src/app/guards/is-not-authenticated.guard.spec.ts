import {IsNotAuthenticatedGuard} from './is-not-authenticated.guard';
import {NavigationExtras, Router} from '@angular/router';
import {TokenService} from '../services/token.service';
import {UserService} from '../services/user.service';
import {SnackbarService} from '../services/snackbar.service';

describe('IsNotAuthenticatedGuard', () => {
  let tokenService: TokenService;
  let userService: UserService;
  let router: Router;
  let guard: IsNotAuthenticatedGuard;
  let snackbar: SnackbarService;

  beforeEach(() => {
    userService = {isHealthDepartmentUser: () => true} as any;
    tokenService = {isAuthenticated: () => true} as any;
    router = {
      navigate: (commands: any[], extras?: NavigationExtras) => {
      }
    } as Router;
    snackbar = {message: () => true} as any;

    guard = new IsNotAuthenticatedGuard(tokenService, userService, router, snackbar);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
