import { async, TestBed } from '@angular/core/testing';
import { AuthDomainModule } from './auth-domain.module';

describe('AuthDomainModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AuthDomainModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AuthDomainModule).toBeDefined();
  });
});
