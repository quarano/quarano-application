/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { IndexCaseService } from './index-case.service';

describe('Service: IndexCase', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [IndexCaseService]
    });
  });

  it('should ...', inject([IndexCaseService], (service: IndexCaseService) => {
    expect(service).toBeTruthy();
  }));
});
