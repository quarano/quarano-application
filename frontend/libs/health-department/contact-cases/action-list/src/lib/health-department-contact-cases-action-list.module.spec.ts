import { async, TestBed } from '@angular/core/testing';
import { HealthDepartmentContactCasesActionListModule } from './health-department-contact-cases-action-list.module';

describe('HealthDepartmentContactCasesActionListModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HealthDepartmentContactCasesActionListModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(HealthDepartmentContactCasesActionListModule).toBeDefined();
  });
});
