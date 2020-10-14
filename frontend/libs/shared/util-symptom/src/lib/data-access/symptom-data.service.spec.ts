/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { SymptomDataService } from './symptom-data.service';
import { HttpClientModule } from '@angular/common/http';
import { EntityCollectionServiceBase, HttpUrlGenerator } from '@ngrx/data';
import { InjectionToken } from '@angular/core';
import { API_URL } from '@qro/shared/util-data-access';

describe('Service: SymptomData', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [SymptomDataService, { provide: HttpUrlGenerator, useValue: {} }, { provide: API_URL, useValue: '' }],
    });
  });

  xit('should ...', inject([SymptomDataService], (service: SymptomDataService) => {
    expect(service).toBeTruthy();
  }));
});
