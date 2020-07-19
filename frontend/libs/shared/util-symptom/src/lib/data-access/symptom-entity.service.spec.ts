/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { SymptomEntityService } from './symptom-entity.service';

describe('Service: SymptomEntity', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SymptomEntityService],
    });
  });

  it('should ...', inject([SymptomEntityService], (service: SymptomEntityService) => {
    expect(service).toBeTruthy();
  }));
});
