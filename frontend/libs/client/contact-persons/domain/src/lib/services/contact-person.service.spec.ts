/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ContactPersonService } from './contact-person.service';

describe('Service: ContactPerson', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ContactPersonService],
    });
  });

  it('should ...', inject([ContactPersonService], (service: ContactPersonService) => {
    expect(service).toBeTruthy();
  }));
});
