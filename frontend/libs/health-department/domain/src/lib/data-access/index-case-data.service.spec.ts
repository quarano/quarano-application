/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { IndexCaseDataService } from './index-case-data.service';

describe('Service: IndexCaseData', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [IndexCaseDataService],
    });
  });

  it('should ...', inject([IndexCaseDataService], (service: IndexCaseDataService) => {
    expect(service).toBeTruthy();
  }));
});
