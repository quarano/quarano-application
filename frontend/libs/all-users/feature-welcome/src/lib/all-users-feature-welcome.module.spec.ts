import { async, TestBed } from '@angular/core/testing';
import { AllUsersFeatureWelcomeModule } from './all-users-feature-welcome.module';

describe('AllUsersFeatureWelcomeModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AllUsersFeatureWelcomeModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AllUsersFeatureWelcomeModule).toBeDefined();
  });
});
