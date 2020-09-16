import { TestBed } from '@angular/core/testing';

import { ContactLocationService } from './contact-location.service';

describe('ContactLocationService', () => {
  let service: ContactLocationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContactLocationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
