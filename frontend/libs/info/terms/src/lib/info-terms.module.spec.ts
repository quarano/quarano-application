import { async, TestBed } from '@angular/core/testing';
import { InfoTermsModule } from './info-terms.module';

describe('InfoTermsModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [InfoTermsModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(InfoTermsModule).toBeDefined();
  });
});
