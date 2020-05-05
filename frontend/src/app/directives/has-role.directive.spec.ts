/* tslint:disable:no-unused-variable */

import { TestBed, async } from '@angular/core/testing';
import { HasRoleDirective } from './has-role.directive';
import {TemplateRef, ViewContainerRef} from '@angular/core';
import {UserService} from '@services/user.service';
import {EnrollmentService} from '@services/enrollment.service';

describe('Directive: HasRole', () => {
  let viewContainerRef: ViewContainerRef;
  let templateRef: TemplateRef<any>;
  let userService: UserService;

  beforeEach(() => {
    viewContainerRef = jasmine.createSpyObj<ViewContainerRef>(['createEmbeddedView']);
    templateRef = {} as TemplateRef<any>;
    userService = jasmine.createSpyObj<UserService>(['roleMatch']);
  });

  it('should create an instance', () => {
    const directive = new HasRoleDirective(viewContainerRef, templateRef, userService);
    expect(directive).toBeTruthy();
  });
});
