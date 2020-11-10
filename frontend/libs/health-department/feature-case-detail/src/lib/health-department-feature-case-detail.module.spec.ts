import { async, TestBed } from '@angular/core/testing';
import { HealthDepartmentFeatureCaseDetailModule } from './health-department-feature-case-detail.module';
import { ActivatedRoute } from '@angular/router';

describe('HealthDepartmentFeatureCaseDetailModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HealthDepartmentFeatureCaseDetailModule],
      providers: [{ provide: ActivatedRoute, useValue: {} }],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(HealthDepartmentFeatureCaseDetailModule).toBeDefined();
  });
});
