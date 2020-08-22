import { UserService } from './user.service';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { TokenService } from './token.service';
import { AuthService } from './auth.service';
import { provideMockStore } from '@ngrx/store/testing';
import { TestBed, inject } from '@angular/core/testing';
import { Store } from '@ngrx/store';

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
    const store = provideMockStore({});
    TestBed.configureTestingModule({
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: SnackbarService, useValue: snackbarService },
        { provide: Store, useValue: store },
        { provide: TokenService, useValue: tokenService },
      ],
    });
  });

  it('should ...', inject([UserService], (service: UserService) => {
    expect(service).toBeTruthy();
  }));
});
