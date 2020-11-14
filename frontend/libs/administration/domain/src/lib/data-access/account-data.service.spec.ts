import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpUrlGenerator } from '@ngrx/data';
import { AccountDataService } from './account-data.service';

describe('Service: IndexCaseData', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AccountDataService, { provide: HttpUrlGenerator, useValue: {} }],
      imports: [HttpClientTestingModule],
    });
  });

  xit('should ...', inject([AccountDataService], (service: AccountDataService) => {
    expect(service).toBeTruthy();
  }));
});
