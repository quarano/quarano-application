import { SnackbarService } from '@services/snackbar.service';
import { IsNotAuthenticatedGuard } from './is-not-authenticated.guard';
import { TokenService } from '@services/token.service';
import { UserService } from '@services/user.service';
import { Router } from '@angular/router';

describe('IsNotAuthenticatedGuard', () => {
  let tokenService: TokenService;
  let userService: UserService;
  let router: Router;
  let guard: IsNotAuthenticatedGuard;
  let snackbar: SnackbarService;

  beforeEach(() => {
    userService = jasmine.createSpyObj(['isHealthDepartmentUser']);
    tokenService = jasmine.createSpyObj(['isAuthenticated']);
    router = jasmine.createSpyObj(['navigate']);
    snackbar = jasmine.createSpyObj(['message']);

    guard = new IsNotAuthenticatedGuard(tokenService, userService, router, snackbar);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
