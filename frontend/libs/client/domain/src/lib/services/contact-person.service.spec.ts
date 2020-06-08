import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed, async, inject } from '@angular/core/testing';
import { ContactPersonService } from './contact-person.service';
import { API_URL } from '@qro/shared/util';

describe('Service: ContactPerson', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ContactPersonService, { provide: API_URL, useValue: '' }],
    });
  });

  it('should ...', inject([ContactPersonService], (service: ContactPersonService) => {
    expect(service).toBeTruthy();
  }));
});
