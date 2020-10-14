/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { CaseDataService } from './case-data.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpUrlGenerator } from '@ngrx/data';

describe('Service: IndexCaseData', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CaseDataService, { provide: HttpUrlGenerator, useValue: {} }],
      imports: [HttpClientTestingModule],
    });
  });

  xit('should ...', inject([CaseDataService], (service: CaseDataService) => {
    expect(service).toBeTruthy();
  }));
});
