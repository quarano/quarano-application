import { async, TestBed } from '@angular/core/testing';
import { ClientFeatureHealthDepartmentContactModule } from './client-feature-health-department-contact.module';

describe('ClientFeatureHealthDepartmentContactModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientFeatureHealthDepartmentContactModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientFeatureHealthDepartmentContactModule).toBeDefined();
  });
});
