/* tslint:disable:no-unused-variable */
import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ForgottenContactBannerComponent} from './forgotten-contact-banner.component';
import {MatDialog} from '@angular/material/dialog';
import {ApiService} from '../../../services/api.service';
import {NO_ERRORS_SCHEMA} from '@angular/core';

describe('ForgottenContactBannerComponent', () => {
  let component: ForgottenContactBannerComponent;
  let fixture: ComponentFixture<ForgottenContactBannerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ForgottenContactBannerComponent],
      providers: [
        {provide: MatDialog, useValue: {}},
        {provide: ApiService, useValue: {}}
      ],
      schemas: [NO_ERRORS_SCHEMA]
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
