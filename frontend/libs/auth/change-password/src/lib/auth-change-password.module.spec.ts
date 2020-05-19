import { async, TestBed } from '@angular/core/testing';
import { AuthChangePasswordModule } from './auth-change-password.module';

describe('AuthChangePasswordModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AuthChangePasswordModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AuthChangePasswordModule).toBeDefined();
  });
});
