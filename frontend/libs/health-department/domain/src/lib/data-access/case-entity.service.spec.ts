/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { CaseEntityService } from './case-entity.service';
import { EntityCollectionServiceElementsFactory, HttpUrlGenerator } from '@ngrx/data';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('Service: IndexCaseEntity', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        CaseEntityService,
        { provide: EntityCollectionServiceElementsFactory, useValue: {} },
        { provide: HttpUrlGenerator, useValue: {} },
      ],
      imports: [HttpClientTestingModule],
    });
  });

  xit('should ...', inject([CaseEntityService], (service: CaseEntityService) => {
    expect(service).toBeTruthy();
  }));
});
