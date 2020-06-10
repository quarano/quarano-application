import { async, TestBed } from '@angular/core/testing';
import { AllUsersShellModule } from './all-users-shell.module';

describe('AllUsersShellModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AllUsersShellModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AllUsersShellModule).toBeDefined();
  });
});
