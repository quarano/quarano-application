import { async, TestBed } from '@angular/core/testing';
import { HealthDepartmentIndexCasesDomainModule } from './health-department-index-cases-domain.module';

describe('HealthDepartmentIndexCasesDomainModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HealthDepartmentIndexCasesDomainModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(HealthDepartmentIndexCasesDomainModule).toBeDefined();
  });
});
