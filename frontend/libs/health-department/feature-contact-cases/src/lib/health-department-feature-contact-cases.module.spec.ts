import { async, TestBed } from '@angular/core/testing';
import { HealthDepartmentFeatureContactCasesModule } from './health-department-feature-contact-cases.module';

describe('HealthDepartmentFeatureContactCasesModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HealthDepartmentFeatureContactCasesModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(HealthDepartmentFeatureContactCasesModule).toBeDefined();
  });
});
