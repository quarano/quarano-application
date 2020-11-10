import { async, TestBed } from '@angular/core/testing';
import { GeneralFeatureWelcomeModule } from './general-feature-welcome.module';

describe('GeneralFeatureWelcomeModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [GeneralFeatureWelcomeModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(GeneralFeatureWelcomeModule).toBeDefined();
  });
});
