import { TestBed } from '@angular/core/testing';

import { IsAuthenticatedGuard } from './is-authenticated.guard';
import {of} from 'rxjs';
import {UserService} from '@services/user.service';

describe('IsAuthenticatedGuard', () => {
  let guard: IsAuthenticatedGuard;

  beforeEach(() => {
    const userService = {isLoggedIn$: of()} as UserService;
    const router = jasmine.createSpyObj(['navigate']);
    const snackbarService = jasmine.createSpyObj(['message']);
    guard = new IsAuthenticatedGuard(userService, router, snackbarService);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
