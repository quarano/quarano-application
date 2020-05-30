import { async, TestBed } from '@angular/core/testing';
import { AuthLoginModule } from './auth-login.module';

describe('AuthLoginModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AuthLoginModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AuthLoginModule).toBeDefined();
  });
});
