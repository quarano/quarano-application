import { async, TestBed } from '@angular/core/testing';
import { AdministrationAccountsAccountListModule } from './administration-accounts-account-list.module';

describe('AdministrationAccountsAccountListModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AdministrationAccountsAccountListModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AdministrationAccountsAccountListModule).toBeDefined();
  });
});
