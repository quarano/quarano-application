import { async, TestBed } from '@angular/core/testing';
import { HealthDepartmentContactCasesCaseListModule } from './health-department-contact-cases-case-list.module';

describe('HealthDepartmentContactCasesCaseListModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HealthDepartmentContactCasesCaseListModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(HealthDepartmentContactCasesCaseListModule).toBeDefined();
  });
});
