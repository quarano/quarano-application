/* tslint:disable:no-unused-variable */

import { TestBed, async } from '@angular/core/testing';
import { AsideHostDirective } from './aside-host.directive';
import {TemplateRef, ViewContainerRef} from '@angular/core';
import {UserService} from '@services/user.service';
import {EnrollmentService} from '@services/enrollment.service';

describe('Directive: Aside', () => {
  let viewContainerRef: ViewContainerRef;

  beforeEach(() => {
    viewContainerRef = jasmine.createSpyObj<ViewContainerRef>(['createEmbeddedView']);
  });

  it('should create an instance', () => {
    const directive = new AsideHostDirective(viewContainerRef);
    expect(directive).toBeTruthy();
  });
});
