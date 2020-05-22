import { async, TestBed } from '@angular/core/testing';
import { HealthDepartmentContactCasesShellModule } from './health-department-contact-cases-shell.module';

describe('HealthDepartmentContactCasesShellModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HealthDepartmentContactCasesShellModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(HealthDepartmentContactCasesShellModule).toBeDefined();
  });
});
