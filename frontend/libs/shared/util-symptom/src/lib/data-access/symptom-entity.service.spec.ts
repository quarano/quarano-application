/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { SymptomEntityService } from './symptom-entity.service';
import { EntityCollectionServiceElementsFactory } from '@ngrx/data';

describe('Service: SymptomEntity', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SymptomEntityService, { provide: EntityCollectionServiceElementsFactory, useValue: {} }],
    });
  });

  xit('should ...', inject([SymptomEntityService], (service: SymptomEntityService) => {
    expect(service).toBeTruthy();
  }));
});
