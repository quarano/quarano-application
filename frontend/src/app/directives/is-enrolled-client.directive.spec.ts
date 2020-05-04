/* tslint:disable:no-unused-variable */

import { TestBed, async } from '@angular/core/testing';
import { IsEnrolledClientDirective } from './is-enrolled-client.directive';
import {TemplateRef, ViewContainerRef} from '@angular/core';
import {UserService} from '@services/user.service';
import {EnrollmentService} from '@services/enrollment.service';

describe('Directive: IsEnrolledClient', () => {
  let viewContainerRef: ViewContainerRef;
  let templateRef: TemplateRef<any>;
  let userService: UserService;
  let enrollmentService: EnrollmentService;

  beforeEach(() => {
    viewContainerRef = jasmine.createSpyObj<ViewContainerRef>(['createEmbeddedView']);
    templateRef = {} as TemplateRef<any>;
    userService = {isHealthDepartmentUser: false} as UserService;
    enrollmentService = jasmine.createSpyObj<EnrollmentService>(['getEnrollmentStatus']);
  });

  it('should create an instance', () => {
    const directive = new IsEnrolledClientDirective(viewContainerRef, templateRef, userService, enrollmentService);
    expect(directive).toBeTruthy();
  });
});
