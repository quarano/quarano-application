import { async, TestBed } from '@angular/core/testing';
import { AllUsersFeatureImprintModule } from './all-users-feature-imprint.module';

describe('AllUsersFeatureImprintModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AllUsersFeatureImprintModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AllUsersFeatureImprintModule).toBeDefined();
  });
});
