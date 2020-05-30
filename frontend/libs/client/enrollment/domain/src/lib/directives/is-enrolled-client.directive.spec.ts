/* tslint:disable:no-unused-variable */

import { IsEnrolledClientDirective } from './is-enrolled-client.directive';
import { TemplateRef } from '@angular/core';
import { UserService } from '../../../../../../auth/domain/src/lib/services/user.service';
import { EnrollmentService } from '../services/enrollment.service';

describe('Directive: IsEnrolledClient', () => {
  let templateRef: TemplateRef<any>;
  let userService: UserService;
  let enrollmentService: EnrollmentService;

  beforeEach(() => {
    templateRef = {} as TemplateRef<any>;
    userService = { isHealthDepartmentUser: false } as UserService;
    enrollmentService = {
      getEnrollmentStatus: () => {
      }
    } as any;
  });

  it('should create an instance', () => {
    const directive = new IsEnrolledClientDirective(null, templateRef, userService, enrollmentService);
    expect(directive).toBeTruthy();
  });
});
