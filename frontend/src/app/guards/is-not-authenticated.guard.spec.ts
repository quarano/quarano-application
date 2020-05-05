import { TestBed } from '@angular/core/testing';

import { IsNotAuthenticatedGuard } from './is-not-authenticated.guard';
import {TokenService} from '@services/token.service';
import {UserService} from '@services/user.service';
import {Router} from '@angular/router';

describe('IsNotAuthenticatedGuard', () => {
  let tokenService: TokenService;
  let userService: UserService;
  let router: Router;
  let guard: IsNotAuthenticatedGuard;

  beforeEach(() => {
    userService = jasmine.createSpyObj(['isHealthDepartmentUser']);
    tokenService = jasmine.createSpyObj(['isAuthenticated']);
    router = jasmine.createSpyObj(['navigate']);

    guard = new IsNotAuthenticatedGuard(tokenService, userService, router);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
