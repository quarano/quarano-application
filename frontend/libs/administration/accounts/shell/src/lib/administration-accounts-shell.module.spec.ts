import { async, TestBed } from '@angular/core/testing';
import { AdministrationAccountsShellModule } from './administration-accounts-shell.module';

describe('AdministrationAccountsShellModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AdministrationAccountsShellModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AdministrationAccountsShellModule).toBeDefined();
  });
});
