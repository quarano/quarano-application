import { TestBed } from '@angular/core/testing';

import { OccasionService } from './occasion.service';

describe('OccasionService', () => {
  let service: OccasionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OccasionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
