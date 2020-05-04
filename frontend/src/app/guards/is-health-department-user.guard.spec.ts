import {TestBed} from '@angular/core/testing';

import {IsHealthDepartmentUserGuard} from './is-health-department-user.guard';
import { UserService } from '@services/user.service';

describe('IsHealthDepartmentUserGuard', () => {
  let guard: IsHealthDepartmentUserGuard;
  let userService: UserService;

  beforeEach(() => {
    userService = jasmine.createSpyObj<UserService>(['isHealthDepartmentUser']);
    guard = new IsHealthDepartmentUserGuard(userService);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
