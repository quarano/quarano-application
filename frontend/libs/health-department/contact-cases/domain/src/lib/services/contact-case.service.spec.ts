/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ContactCaseService } from './contact-case.service';

describe('Service: ContactCase', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ContactCaseService]
    });
  });

  it('should ...', inject([ContactCaseService], (service: ContactCaseService) => {
    expect(service).toBeTruthy();
  }));
});
