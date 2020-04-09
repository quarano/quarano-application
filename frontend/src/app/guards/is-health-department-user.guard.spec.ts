import {TestBed} from '@angular/core/testing';

import {IsHealthDepartmentUserGuard} from './is-health-department-user.guard';

describe('IsHealthDepartmentUserGuard', () => {
  let guard: IsHealthDepartmentUserGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(IsHealthDepartmentUserGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
