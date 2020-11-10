import { UserService, AuthStore } from '@qro/auth/api';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed, inject } from '@angular/core/testing';
import { HealthDepartmentService } from './health-department.service';
import { API_URL } from '@qro/shared/util-data-access';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('Service: HealthDepartment', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        HealthDepartmentService,
        { provide: API_URL, useValue: '' },
        { provide: UserService, useValue: {} },
        { provide: AuthStore, useValue: {} },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    });
  });

  it('should ...', inject([HealthDepartmentService], (service: HealthDepartmentService) => {
    expect(service).toBeTruthy();
  }));
});
