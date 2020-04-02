import { TestBed } from '@angular/core/testing';

import { IsNotAuthenticatedGuard } from './is-not-authenticated.guard';

describe('IsNotAuthenticatedGuard', () => {
  let guard: IsNotAuthenticatedGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(IsNotAuthenticatedGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
