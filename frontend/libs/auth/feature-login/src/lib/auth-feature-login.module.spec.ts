import { async, TestBed } from '@angular/core/testing';
import { AuthFeatureLoginModule } from './auth-feature-login.module';

describe('AuthFeatureLoginModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AuthFeatureLoginModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AuthFeatureLoginModule).toBeDefined();
  });
});
