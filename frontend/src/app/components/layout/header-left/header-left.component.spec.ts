/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { HeaderLeftComponent } from './header-left.component';
import {UserService} from '@services/user.service';
import {Observable, of} from 'rxjs';

describe('HeaderLeftComponent', () => {
  let component: HeaderLeftComponent;
  let fixture: ComponentFixture<HeaderLeftComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HeaderLeftComponent ],
      providers: [
        {provide: UserService, useValue: { isLoggedIn$: of() }}
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderLeftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
