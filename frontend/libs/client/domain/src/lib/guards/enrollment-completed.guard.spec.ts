import { ClientStore } from './../store/client-store.service';
import { EnrollmentCompletedGuard } from './enrollment-completed.guard';
import { Router } from '@angular/router';
import { UserService } from '@qro/auth/api';
import { SnackbarService } from '@qro/shared/util-snackbar';

describe('IsAuthenticatedFullyClientGuard', () => {
  let userService: UserService;
  let clientStore: ClientStore;
  let router: Router;
  let snackbarService: SnackbarService;
  let guard: EnrollmentCompletedGuard;

  beforeEach(() => {
    userService = { isHealthDepartmentUser: () => true } as any;
    clientStore = {} as any;
    router = { navigate: () => {} } as any;
    snackbarService = { message: () => true } as any;

    guard = new EnrollmentCompletedGuard(clientStore, userService, router, snackbarService);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
