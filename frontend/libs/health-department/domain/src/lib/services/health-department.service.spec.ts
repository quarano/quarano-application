/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { HealthDepartmentService } from './health-department.service';

describe('Service: HealthDepartment', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HealthDepartmentService],
    });
  });

  it('should ...', inject([HealthDepartmentService], (service: HealthDepartmentService) => {
    expect(service).toBeTruthy();
  }));
});
