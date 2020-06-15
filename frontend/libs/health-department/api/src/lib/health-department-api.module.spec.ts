import { async, TestBed } from '@angular/core/testing';
import { HealthDepartmentApiModule } from './health-department-api.module';

describe('HealthDepartmentApiModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HealthDepartmentApiModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(HealthDepartmentApiModule).toBeDefined();
  });
});
