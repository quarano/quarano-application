import { async, TestBed } from '@angular/core/testing';
import { HealthDepartmentIndexCasesCaseListModule } from './health-department-index-cases-case-list.module';

describe('HealthDepartmentIndexCasesCaseListModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HealthDepartmentIndexCasesCaseListModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(HealthDepartmentIndexCasesCaseListModule).toBeDefined();
  });
});
