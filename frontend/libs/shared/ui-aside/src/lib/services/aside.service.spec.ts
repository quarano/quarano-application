import { inject, TestBed } from '@angular/core/testing';
import { AsideService } from './aside.service';

describe('Service: Aside', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AsideService],
    });
  });

  it('should ...', inject([AsideService], (service: AsideService) => {
    expect(service).toBeTruthy();
  }));
});
