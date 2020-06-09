import { API_URL } from '@qro/shared/util';
import { HttpClientTestingModule } from '@angular/common/http/testing';
/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ContactCaseService } from './contact-case.service';

describe('Service: ContactCase', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ContactCaseService, { provide: API_URL, useValue: '' }],
    });
  });

  it('should ...', inject([ContactCaseService], (service: ContactCaseService) => {
    expect(service).toBeTruthy();
  }));
});
