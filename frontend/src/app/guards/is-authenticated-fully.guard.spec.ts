import { TestBed } from '@angular/core/testing';

import { IsAuthenticatedFullyGuard } from './is-authenticated-fully.guard';

describe('IsAuthenticatedFullyGuard', () => {
  let guard: IsAuthenticatedFullyGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(IsAuthenticatedFullyGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
