import { IsNotAuthenticatedGuard } from './is-not-authenticated.guard';
import { Router } from '@angular/router';
import { TokenService } from '../services/token.service';
import { SnackbarService } from '../services/snackbar.service';

describe('IsNotAuthenticatedGuard', () => {
  let tokenService: TokenService;
  let router: Router;
  let guard: IsNotAuthenticatedGuard;
  let snackbar: SnackbarService;

  beforeEach(() => {
    tokenService = { isAuthenticated: () => true } as any;
    router = { navigate: () => { } } as any;
    snackbar = { message: () => true } as any;

    guard = new IsNotAuthenticatedGuard(tokenService, router, snackbar);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
