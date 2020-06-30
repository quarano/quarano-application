import { async, TestBed } from '@angular/core/testing';
import { GeneralFeatureDataProtectionModule } from './general-feature-data-protection.module';

describe('GeneralFeatureDataProtectionModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [GeneralFeatureDataProtectionModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(GeneralFeatureDataProtectionModule).toBeDefined();
  });
});
