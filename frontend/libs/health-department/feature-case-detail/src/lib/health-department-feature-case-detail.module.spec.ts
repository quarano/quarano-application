import { async, TestBed } from '@angular/core/testing';
import { HealthDepartmentFeatureCaseDetailModule } from './health-department-feature-case-detail.module';

describe('HealthDepartmentFeatureCaseDetailModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HealthDepartmentFeatureCaseDetailModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(HealthDepartmentFeatureCaseDetailModule).toBeDefined();
  });
});
