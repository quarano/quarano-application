/* tslint:disable:no-unused-variable */

import { TestBed, inject } from '@angular/core/testing';
import { AccountService } from './account.service';
import { API_URL } from '@qro/shared/util';
import { HttpClientModule } from '@angular/common/http';

describe('Service: Account', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [AccountService, { provide: API_URL, useValue: '' },
      ]
    });
  });

  it('should ...', inject([AccountService], (service: AccountService) => {
    expect(service).toBeTruthy();
  }));
});
