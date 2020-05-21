import { async, TestBed } from '@angular/core/testing';
import { HealthDepartmentIndexCasesModule } from './health-department-index-cases.module';

describe('HealthDepartmentIndexCasesModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HealthDepartmentIndexCasesModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(HealthDepartmentIndexCasesModule).toBeDefined();
  });
});
