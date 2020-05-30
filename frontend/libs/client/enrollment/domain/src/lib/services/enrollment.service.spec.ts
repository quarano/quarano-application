/* tslint:disable:no-unused-variable */

import {inject, TestBed} from '@angular/core/testing';
import {EnrollmentService} from './enrollment.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('Service: Enrollment', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [EnrollmentService]
    });
  });

  it('should ...', inject([EnrollmentService], (service: EnrollmentService) => {
    expect(service).toBeTruthy();
  }));
});
