import { async, TestBed } from '@angular/core/testing';
import { AllUsersFeatureTermsModule } from './all-users-feature-terms.module';

describe('AllUsersFeatureTermsModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AllUsersFeatureTermsModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AllUsersFeatureTermsModule).toBeDefined();
  });
});
