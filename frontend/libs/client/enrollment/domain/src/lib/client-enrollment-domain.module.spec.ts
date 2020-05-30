import { async, TestBed } from '@angular/core/testing';
import { ClientEnrollmentDomainModule } from './client-enrollment-domain.module';

describe('ClientEnrollmentDomainModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientEnrollmentDomainModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientEnrollmentDomainModule).toBeDefined();
  });
});
