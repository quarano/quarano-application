import { async, TestBed } from '@angular/core/testing';
import { HealthDepartmentIndexCasesActionListModule } from './health-department-index-cases-action-list.module';

describe('HealthDepartmentIndexCasesActionListModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HealthDepartmentIndexCasesActionListModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(HealthDepartmentIndexCasesActionListModule).toBeDefined();
  });
});
