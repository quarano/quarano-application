import { AuthStore } from './../store/auth-store.service';
import { UserService } from './user.service';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { TokenService } from './token.service';
import { AuthService } from './auth.service';
import { TestBed, inject } from '@angular/core/testing';

describe('UserService', () => {
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

    TestBed.configureTestingModule({
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: SnackbarService, useValue: snackbarService },
        { provide: AuthStore, useValue: {} },
        { provide: TokenService, useValue: tokenService },
      ],
    });
  });

  it('should ...', inject([UserService], (service: UserService) => {
    expect(service).toBeTruthy();
  }));
});
