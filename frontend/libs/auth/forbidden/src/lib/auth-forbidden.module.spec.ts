import { async, TestBed } from '@angular/core/testing';
import { AuthForbiddenModule } from './auth-forbidden.module';

describe('AuthForbiddenModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AuthForbiddenModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AuthForbiddenModule).toBeDefined();
  });
});
