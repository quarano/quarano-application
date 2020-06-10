import { async, TestBed } from '@angular/core/testing';
import { AllUsersFeatureLoginModule } from './all-users-feature-login.module';

describe('AllUsersFeatureLoginModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AllUsersFeatureLoginModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AllUsersFeatureLoginModule).toBeDefined();
  });
});
