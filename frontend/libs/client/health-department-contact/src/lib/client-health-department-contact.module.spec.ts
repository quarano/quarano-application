import { async, TestBed } from '@angular/core/testing';
import { ClientHealthDepartmentContactModule } from './client-health-department-contact.module';

describe('ClientHealthDepartmentContactModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ClientHealthDepartmentContactModule],
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ClientHealthDepartmentContactModule).toBeDefined();
  });
});
