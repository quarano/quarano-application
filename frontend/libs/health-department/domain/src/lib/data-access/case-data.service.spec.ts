/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { CaseDataService } from './case-data.service';

describe('Service: IndexCaseData', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CaseDataService],
    });
  });

  it('should ...', inject([CaseDataService], (service: CaseDataService) => {
    expect(service).toBeTruthy();
  }));
});
