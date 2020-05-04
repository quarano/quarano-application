import { TestBed } from '@angular/core/testing';

import { EnrollmentCompletedGuard } from './enrollment-completed.guard';
import { UserService } from '@services/user.service';
import { EnrollmentService } from '@services/enrollment.service';
import { Router } from '@angular/router';
import { SnackbarService } from '@services/snackbar.service';

describe('IsAuthenticatedFullyClientGuard', () => {
  let userService: UserService;
  let enrollmentService: EnrollmentService;
  let router: Router;
  let snackbarService: SnackbarService;
  let guard: EnrollmentCompletedGuard;

  beforeEach(() => {
    userService = jasmine.createSpyObj(['isHealthDepartmentUser']);
    enrollmentService = jasmine.createSpyObj(['getEnrollmentStatus']);
    router = jasmine.createSpyObj(['navigate']);
    snackbarService = jasmine.createSpyObj(['message']);

    guard = new EnrollmentCompletedGuard(enrollmentService, userService, router, snackbarService);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
