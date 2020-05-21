import { async, TestBed } from '@angular/core/testing';
import { HealthDepartmentContactCasesModule } from './health-department-contact-cases.module';

describe('HealthDepartmentContactCasesModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HealthDepartmentContactCasesModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(HealthDepartmentContactCasesModule).toBeDefined();
  });
});
