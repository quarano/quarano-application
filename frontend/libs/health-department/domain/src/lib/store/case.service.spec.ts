import { TestBed } from '@angular/core/testing';

import { CaseService } from './case.service';
import { EntityCollectionServiceElementsFactory } from '@ngrx/data';

describe('CaseDetailService', () => {
  let service: CaseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [{ provide: EntityCollectionServiceElementsFactory, useValue: {} }],
    });
    service = TestBed.inject(CaseService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
