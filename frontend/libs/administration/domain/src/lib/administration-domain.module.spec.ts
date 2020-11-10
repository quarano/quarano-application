import { async, TestBed } from '@angular/core/testing';
import { AdministrationDomainModule } from './administration-domain.module';

describe('AdministrationDomainModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AdministrationDomainModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(AdministrationDomainModule).toBeDefined();
  });
});
