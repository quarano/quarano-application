import { SnackbarService } from './../../../../../apps/quarano-frontend/src/app/services/snackbar.service';
/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { BadRequestService } from './bad-request.service';

describe('Service: BadRequest', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [BadRequestService, { provide: SnackbarService, useValue: {} }]
    });
  });

  it('should ...', inject([BadRequestService], (service: BadRequestService) => {
    expect(service).toBeTruthy();
  }));
});
