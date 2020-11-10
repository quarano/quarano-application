import { async, TestBed } from '@angular/core/testing';
import { HealthDepartmentDomainModule } from './health-department-domain.module';

describe('HealthDepartmentDomainModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HealthDepartmentDomainModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(HealthDepartmentDomainModule).toBeDefined();
  });
});
