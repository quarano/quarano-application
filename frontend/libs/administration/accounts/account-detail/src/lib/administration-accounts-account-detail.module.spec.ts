import { async, TestBed } from '@angular/core/testing';
import { AdministrationAccountsAccountDetailModule } from './administration-accounts-account-detail.module';

describe('AdministrationAccountsAccountDetailModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AdministrationAccountsAccountDetailModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AdministrationAccountsAccountDetailModule).toBeDefined();
  });
});
