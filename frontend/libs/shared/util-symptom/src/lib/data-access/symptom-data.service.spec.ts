/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { SymptomDataService } from './symptom-data.service';

describe('Service: SymptomData', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SymptomDataService],
    });
  });

  it('should ...', inject([SymptomDataService], (service: SymptomDataService) => {
    expect(service).toBeTruthy();
  }));
});
