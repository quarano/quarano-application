import { provideMockStore } from '@ngrx/store/testing';
import { TestBed, async, inject } from '@angular/core/testing';
import { SymptomService } from './symptom.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { API_URL } from '@qro/shared/util-data-access';

describe('Service: Symptom', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SymptomService, { provide: API_URL, useValue: '' }, provideMockStore({})],
    });
  });

  it('should ...', inject([SymptomService], (service: SymptomService) => {
    expect(service).toBeTruthy();
  }));
});
