/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { CaseEntityService } from './case-entity.service';

describe('Service: IndexCaseEntity', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CaseEntityService],
    });
  });

  it('should ...', inject([CaseEntityService], (service: CaseEntityService) => {
    expect(service).toBeTruthy();
  }));
});
