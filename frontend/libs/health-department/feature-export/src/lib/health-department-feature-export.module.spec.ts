import { async, TestBed } from '@angular/core/testing';
import { HealthDepartmentFeatureExportModule } from './health-department-feature-export.module';

describe('FeatureExportModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HealthDepartmentFeatureExportModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(HealthDepartmentFeatureExportModule).toBeDefined();
  });
});
