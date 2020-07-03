import { IsAuthenticatedGuard } from './is-authenticated.guard';
import { UserService } from '../data-access/user.service';
import { Router } from '@angular/router';
import { SnackbarService } from '@qro/shared/util-snackbar';

describe('IsAuthenticatedGuard', () => {
  let userService: UserService;
  let router: Router;
  let snackbar: SnackbarService;
  let guard: IsAuthenticatedGuard;

  beforeEach(() => {
    userService = { isHealthDepartmentUser: () => true } as any;
    router = { navigate: () => {} } as any;
    snackbar = { message: () => true } as any;
    guard = new IsAuthenticatedGuard(userService, router, snackbar);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
