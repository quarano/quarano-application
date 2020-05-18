import {IsAuthenticatedGuard} from './is-authenticated.guard';
import {UserService} from '../services/user.service';
import {NavigationExtras, Router} from '@angular/router';
import {SnackbarService} from '../services/snackbar.service';

describe('IsAuthenticatedGuard', () => {
  let userService: UserService;
  let router: Router;
  let snackbar: SnackbarService;
  let guard: IsAuthenticatedGuard;

  beforeEach(() => {
    userService = {isHealthDepartmentUser: () => true} as any;
    router = {
      navigate: (commands: any[], extras?: NavigationExtras) => {
      }
    } as Router;
    snackbar = {message: () => true} as any;
    guard = new IsAuthenticatedGuard(userService, router, snackbar);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
