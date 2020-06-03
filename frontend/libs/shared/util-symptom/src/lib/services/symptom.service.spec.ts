import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed, async, inject } from '@angular/core/testing';
import { SymptomService } from './symptom.service';
import { API_URL } from '@qro/shared/util';

describe('Service: Symptom', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SymptomService, { provide: API_URL, useValue: '' }],
    });
  });

  it('should ...', inject([SymptomService], (service: SymptomService) => {
    expect(service).toBeTruthy();
  }));
});
