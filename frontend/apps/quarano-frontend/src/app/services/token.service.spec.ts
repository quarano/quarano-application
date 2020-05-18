import {TokenService} from './token.service';
import {SnackbarService} from './snackbar.service';

describe('TokenService', () => {
  let service: TokenService;

  beforeEach(() => {
    const snackbarService: SnackbarService = {
      success: () => {
      },
      warning: () => {
      },
      message: () => {
      }
    } as any;
    const router = {
      navigate: () => {
      }
    } as any;
    service = new TokenService(snackbarService, router);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
