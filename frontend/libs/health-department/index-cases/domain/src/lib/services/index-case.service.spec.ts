import { HttpClientTestingModule } from '@angular/common/http/testing';
/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { IndexCaseService } from './index-case.service';
import { API_URL } from '@qro/shared/util';

describe('Service: IndexCase', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [IndexCaseService, { provide: API_URL, useValue: '' }]
    });
  });

  it('should ...', inject([IndexCaseService], (service: IndexCaseService) => {
    expect(service).toBeTruthy();
  }));
});
