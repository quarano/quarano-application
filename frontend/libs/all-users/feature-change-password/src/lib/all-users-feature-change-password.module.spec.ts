import { async, TestBed } from '@angular/core/testing';
import { AllUsersFeatureChangePasswordModule } from './all-users-feature-change-password.module';

describe('AllUsersFeatureChangePasswordModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AllUsersFeatureChangePasswordModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AllUsersFeatureChangePasswordModule).toBeDefined();
  });
});
