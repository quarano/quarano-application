/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { IndexCaseEntityService } from './index-case-entity.service';

describe('Service: IndexCaseEntity', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [IndexCaseEntityService],
    });
  });

  it('should ...', inject([IndexCaseEntityService], (service: IndexCaseEntityService) => {
    expect(service).toBeTruthy();
  }));
});
