/* tslint:disable:no-unused-variable */

import { IsHealthDepartmentUserDirective } from './is-health-department-user.directive';
import { TemplateRef, ViewContainerRef } from '@angular/core';
import { UserService } from '../../../../../auth/domain/src/lib/services/user.service';

describe('Directive: IsHealthDepartmentUser', () => {
  let viewContainerRef: ViewContainerRef;
  let templateRef: TemplateRef<any>;
  let userService: UserService;

  beforeEach(() => {
    viewContainerRef = {} as any;
    templateRef = {} as TemplateRef<any>;
    userService = {} as any;
  });

  it('should create an instance', () => {
    const directive = new IsHealthDepartmentUserDirective(viewContainerRef, templateRef, userService);
    expect(directive).toBeTruthy();
  });
});
