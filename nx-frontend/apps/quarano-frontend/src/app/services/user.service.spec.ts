import {UserService} from './user.service';
import {ApiService} from './api.service';
import {SnackbarService} from './snackbar.service';
import {TokenService} from './token.service';

describe('UserService', () => {
  let service: UserService;

  beforeEach(() => {
    const apiService: ApiService = {getMe: () => null} as any;
    const snackbarService: SnackbarService = {
      success: () => {},
      warning: () => {},
      message: () => {}
    } as any;
    const tokenService: TokenService = {
      unsetToken: () => {
      }
    } as any;
    service = new UserService(apiService, snackbarService, tokenService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
