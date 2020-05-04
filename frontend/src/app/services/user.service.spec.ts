import { TestBed } from '@angular/core/testing';

import { UserService } from './user.service';

describe('UserService', () => {
  let service: UserService;

  beforeEach(() => {
    const apiService = jasmine.createSpyObj(['getMe']);
    const snackbarService = jasmine.createSpyObj(['success', 'warning', 'message']);
    const tokenService = jasmine.createSpyObj(['unsetToken']);
    service = new UserService(apiService, snackbarService, tokenService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
