import { Router } from '@angular/router';
import { IsHealthDepartmentUserGuard } from './is-health-department-user.guard';
import { UserService } from '@qro/auth/api';

describe('IsHealthDepartmentUserGuard', () => {
  let guard: IsHealthDepartmentUserGuard;
  let userService: UserService;
  let router: Router;

  beforeEach(() => {
    userService = { isHealthDepartmentUser: () => true } as any;
    router = { navigate: () => {} } as any;
    guard = new IsHealthDepartmentUserGuard(userService, router);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
