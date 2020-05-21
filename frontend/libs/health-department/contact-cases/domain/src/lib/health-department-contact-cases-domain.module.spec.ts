import { async, TestBed } from '@angular/core/testing';
import { HealthDepartmentContactCasesDomainModule } from './health-department-contact-cases-domain.module';

describe('HealthDepartmentContactCasesDomainModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HealthDepartmentContactCasesDomainModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(HealthDepartmentContactCasesDomainModule).toBeDefined();
  });
});
