import { async, TestBed } from '@angular/core/testing';
import { AllUsersFeatureDataProtectionModule } from './all-users-feature-data-protection.module';

describe('AllUsersFeatureDataProtectionModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AllUsersFeatureDataProtectionModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AllUsersFeatureDataProtectionModule).toBeDefined();
  });
});
