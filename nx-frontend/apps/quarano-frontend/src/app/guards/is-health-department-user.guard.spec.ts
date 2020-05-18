import {NavigationExtras, Router} from '@angular/router';
import { IsHealthDepartmentUserGuard } from './is-health-department-user.guard';
import {UserService} from '../services/user.service';

describe('IsHealthDepartmentUserGuard', () => {
  let guard: IsHealthDepartmentUserGuard;
  let userService: UserService;
  let router: Router;

  beforeEach(() => {
    userService = {isHealthDepartmentUser: () => true} as any;
    router = {
      navigate: (commands: any[], extras?: NavigationExtras) => {
      }
    } as Router;
    guard = new IsHealthDepartmentUserGuard(userService, router);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
