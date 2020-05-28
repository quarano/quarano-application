import { async, TestBed } from '@angular/core/testing';
import { AdministrationAccountsDomainModule } from './administration-accounts-domain.module';

describe('AdministrationDomainModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AdministrationAccountsDomainModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AdministrationAccountsDomainModule).toBeDefined();
  });
});
