import { async, TestBed } from '@angular/core/testing';
import { HealthDepartmentModule } from './health-department.module';

describe('HealthDepartmentModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HealthDepartmentModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(HealthDepartmentModule).toBeDefined();
  });
});
