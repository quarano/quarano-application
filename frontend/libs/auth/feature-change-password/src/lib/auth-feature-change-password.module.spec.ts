import { async, TestBed } from '@angular/core/testing';
import { AuthFeatureChangePasswordModule } from './auth-feature-change-password.module';

describe('AuthFeatureChangePasswordModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AuthFeatureChangePasswordModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AuthFeatureChangePasswordModule).toBeDefined();
  });
});
