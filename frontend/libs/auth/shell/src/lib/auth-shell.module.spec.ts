import { async, TestBed } from '@angular/core/testing';
import { AuthShellModule } from './auth-shell.module';

describe('AuthShellModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AuthShellModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AuthShellModule).toBeDefined();
  });
});
