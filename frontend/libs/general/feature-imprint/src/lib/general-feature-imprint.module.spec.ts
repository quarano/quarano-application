import { async, TestBed } from '@angular/core/testing';
import { GeneralFeatureImprintModule } from './general-feature-imprint.module';

describe('GeneralFeatureImprintModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [GeneralFeatureImprintModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(GeneralFeatureImprintModule).toBeDefined();
  });
});
