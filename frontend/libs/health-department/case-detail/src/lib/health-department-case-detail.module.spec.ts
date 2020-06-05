import { async, TestBed } from '@angular/core/testing';
import { HealthDepartmentCaseDetailModule } from './health-department-case-detail.module';

describe('HealthDepartmentCaseDetailModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HealthDepartmentCaseDetailModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(HealthDepartmentCaseDetailModule).toBeDefined();
  });
});
