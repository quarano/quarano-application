import { TranslateTestingModule } from '@qro/shared/util-translation';
import { AuthStore } from './../store/auth-store.service';
import { UserService } from './user.service';
import { TranslatedSnackbarService } from '@qro/shared/util-snackbar';
import { TokenService } from './token.service';
import { AuthService } from './auth.service';
import { TestBed, inject } from '@angular/core/testing';
import { provideMockStore } from '@ngrx/store/testing';

describe('UserService', () => {
  beforeEach(() => {
    const authService: AuthService = { getMe: () => null } as any;
    const snackbarService: TranslatedSnackbarService = {
      success: () => {},
      warning: () => {},
      message: () => {},
    } as any;
    const tokenService: TokenService = {
      unsetToken: () => {},
    } as any;

    TestBed.configureTestingModule({
      imports: [TranslateTestingModule],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: TranslatedSnackbarService, useValue: snackbarService },
        { provide: AuthStore, useValue: {} },
        { provide: TokenService, useValue: tokenService },
        provideMockStore({}),
      ],
    });
  });

  it('should ...', inject([UserService], (service: UserService) => {
    expect(service).toBeTruthy();
  }));
});
