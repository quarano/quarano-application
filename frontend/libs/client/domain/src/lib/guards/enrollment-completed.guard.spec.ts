import { EnrollmentCompletedGuard } from './enrollment-completed.guard';
import { NavigationExtras, Router } from '@angular/router';
import { UserService } from '../../../../../../auth/domain/src/lib/services/user.service';
import { EnrollmentService } from '../services/enrollment.service';
import { SnackbarService } from '@qro/shared/util';

describe('IsAuthenticatedFullyClientGuard', () => {
  let userService: UserService;
  let enrollmentService: EnrollmentService;
  let router: Router;
  let snackbarService: SnackbarService;
  let guard: EnrollmentCompletedGuard;

  beforeEach(() => {
    userService = { isHealthDepartmentUser: () => true } as any;
    enrollmentService = { getEnrollmentStatus: () => ({}) } as any;
    router = { navigate: () => {} } as any;
    snackbarService = { message: () => true } as any;

    guard = new EnrollmentCompletedGuard(enrollmentService, userService, router, snackbarService);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
