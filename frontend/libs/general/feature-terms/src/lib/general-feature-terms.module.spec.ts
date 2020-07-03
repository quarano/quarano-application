import { async, TestBed } from '@angular/core/testing';
import { GeneralFeatureTermsModule } from './general-feature-terms.module';

describe('GeneralFeatureTermsModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [GeneralFeatureTermsModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(GeneralFeatureTermsModule).toBeDefined();
  });
});
