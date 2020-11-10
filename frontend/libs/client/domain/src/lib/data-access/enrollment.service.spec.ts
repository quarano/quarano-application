import { ClientStore } from './../store/client-store.service';
/* tslint:disable:no-unused-variable */

import { inject, TestBed } from '@angular/core/testing';
import { EnrollmentService } from './enrollment.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { API_URL } from '@qro/shared/util-data-access';

describe('Service: Enrollment', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [EnrollmentService, { provide: API_URL, useValue: '' }, { provide: ClientStore, useValue: {} }],
    });
  });

  it('should ...', inject([EnrollmentService], (service: EnrollmentService) => {
    expect(service).toBeTruthy();
  }));
});
