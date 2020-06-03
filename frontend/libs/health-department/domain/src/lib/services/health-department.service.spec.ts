import { UserService } from '@qro/auth/api';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed, inject } from '@angular/core/testing';
import { HealthDepartmentService } from './health-department.service';
import { API_URL } from '@qro/shared/util';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('Service: HealthDepartment', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [HealthDepartmentService, { provide: API_URL, useValue: '' }, { provide: UserService, useValue: {} }],
      schemas: [NO_ERRORS_SCHEMA],
    });
  });

  it('should ...', inject([HealthDepartmentService], (service: HealthDepartmentService) => {
    expect(service).toBeTruthy();
  }));
});
