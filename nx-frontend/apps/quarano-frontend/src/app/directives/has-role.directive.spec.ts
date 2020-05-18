/* tslint:disable:no-unused-variable */

import {TemplateRef, ViewContainerRef} from '@angular/core';
import {UserService} from '../services/user.service';
import {HasRoleDirective} from './has-role.directive';

describe('Directive: HasRole', () => {
  let viewContainerRef: ViewContainerRef;
  let templateRef: TemplateRef<any>;
  let userService: any;

  beforeEach(() => {
    viewContainerRef = null;
    templateRef = {} as TemplateRef<any>;
    userService = { roleMatch: jest.fn() };
  });

  it('should create an instance', () => {
    const directive = new HasRoleDirective(viewContainerRef, templateRef, userService);
    expect(directive).toBeTruthy();
  });
});
