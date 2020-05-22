import { async, TestBed } from '@angular/core/testing';
import { HealthDepartmentShellModule } from './health-department-shell.module';

describe('HealthDepartmentShellModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HealthDepartmentShellModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(HealthDepartmentShellModule).toBeDefined();
  });
});
