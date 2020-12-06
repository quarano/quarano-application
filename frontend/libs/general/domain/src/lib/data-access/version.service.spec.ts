import { HttpClientTestingModule } from '@angular/common/http/testing';
/* tslint:disable:no-unused-variable */

import { TestBed, inject } from '@angular/core/testing';
import { API_URL } from '@qro/shared/util-data-access';
import { VersionService } from './version.service';

describe('Service: Version', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [VersionService, { provide: API_URL, useValue: '' }],
    });
  });

  it('should ...', inject([VersionService], (service: VersionService) => {
    expect(service).toBeTruthy();
  }));
});
