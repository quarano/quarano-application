import { TestBed } from '@angular/core/testing';

import { TokenService } from './token.service';

describe('TokenService', () => {
  let service: TokenService;

  beforeEach(() => {
    const snackbarService = jasmine.createSpyObj(['success', 'warning', 'message']);
    const router = jasmine.createSpyObj(['navigate']);
    service = new TokenService(snackbarService, router);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
