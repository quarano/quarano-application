import {TestBed} from '@angular/core/testing';

import {IsAuthenticatedFullyClientGuard} from './is-authenticated-fully-client.guard';

describe('IsAuthenticatedFullyClientGuard', () => {
  let guard: IsAuthenticatedFullyClientGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(IsAuthenticatedFullyClientGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
