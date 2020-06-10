import { async, TestBed } from '@angular/core/testing';
import { HealthDepartmentFeatureIndexCasesModule } from './health-department-feature-index-cases.module';

describe('HealthDepartmentFeatureIndexCasesModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HealthDepartmentFeatureIndexCasesModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(HealthDepartmentFeatureIndexCasesModule).toBeDefined();
  });
});
