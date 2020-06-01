/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { SymptomService } from './symptom.service';

describe('Service: Symptom', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SymptomService],
    });
  });

  it('should ...', inject([SymptomService], (service: SymptomService) => {
    expect(service).toBeTruthy();
  }));
});
