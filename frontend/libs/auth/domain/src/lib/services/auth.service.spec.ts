import { UserService } from './../../../../../../apps/quarano-frontend/src/app/services/user.service';
import { API_URL } from '@quarano-frontend/shared/util';
import { HttpClientModule } from '@angular/common/http';
/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { AuthService } from './auth.service';

describe('Service: Auth', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [AuthService, { provide: API_URL, useValue: '' },
      ]
    });
  });

  it('should ...', inject([AuthService], (service: AuthService) => {
    expect(service).toBeTruthy();
  }));
});
