/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { HeaderRightComponent } from './header-right.component';
import {of} from 'rxjs';
import {UserService} from '@services/user.service';
import {MatDialog} from '@angular/material/dialog';

describe('HeaderRightComponent', () => {
  let component: HeaderRightComponent;
  let fixture: ComponentFixture<HeaderRightComponent>;

  beforeEach(async(() => {
    const userService = jasmine.createSpyObj(['logout']);
    userService.isLoggedIn$ = of();

    TestBed.configureTestingModule({
      declarations: [ HeaderRightComponent ],
      providers: [
        {provide: UserService, useValue: userService},
        {provide: MatDialog, useValue: {}}
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderRightComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
