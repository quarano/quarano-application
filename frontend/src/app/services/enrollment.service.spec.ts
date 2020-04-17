/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { EnrollmentService } from './enrollment.service';

describe('Service: Enrollment', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EnrollmentService]
    });
  });

  it('should ...', inject([EnrollmentService], (service: EnrollmentService) => {
    expect(service).toBeTruthy();
  }));
});
