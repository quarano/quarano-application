import { UserService } from './user.service';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { TokenService } from './token.service';
import { AuthService } from './auth.service';

describe('UserService', () => {
  let service: UserService;

  beforeEach(() => {
    const authService: AuthService = { getMe: () => null } as any;
    const snackbarService: SnackbarService = {
      success: () => {},
      warning: () => {},
      message: () => {},
    } as any;
    const tokenService: TokenService = {
      unsetToken: () => {},
    } as any;
    service = new UserService(authService, snackbarService, tokenService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
