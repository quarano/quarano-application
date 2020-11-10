import { async, TestBed } from '@angular/core/testing';
import { HealthDepartmentUiActionAlertModule } from './health-department-ui-action-alert.module';

describe('HealthDepartmentUiActionAlertModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HealthDepartmentUiActionAlertModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(HealthDepartmentUiActionAlertModule).toBeDefined();
  });
});
