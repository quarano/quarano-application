import { async, TestBed } from '@angular/core/testing';
import { HealthDepartmentIndexCasesShellModule } from './health-department-index-cases-shell.module';

describe('HealthDepartmentIndexCasesShellModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HealthDepartmentIndexCasesShellModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(HealthDepartmentIndexCasesShellModule).toBeDefined();
  });
});
