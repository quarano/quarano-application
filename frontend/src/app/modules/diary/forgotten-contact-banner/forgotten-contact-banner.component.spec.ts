/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { ForgottenContactBannerComponent } from './forgotten-contact-banner.component';
import {MatDialog} from '@angular/material/dialog';
import {ApiService} from '@services/api.service';

describe('ForgottenContactBannerComponent', () => {
  let component: ForgottenContactBannerComponent;
  let fixture: ComponentFixture<ForgottenContactBannerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ForgottenContactBannerComponent ],
      providers: [
        {provide: MatDialog, useValue: {}},
        {provide: ApiService, useValue: jasmine.createSpyObj(['getContactPersons'])}
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ForgottenContactBannerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
