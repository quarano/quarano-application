import { TestBed } from '@angular/core/testing';

import { EnrollmentCompletedGuard } from './enrollment-completed.guard';

describe('IsAuthenticatedFullyClientGuard', () => {
  let guard: EnrollmentCompletedGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(EnrollmentCompletedGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
