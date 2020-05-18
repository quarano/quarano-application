import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterComponent } from './register.component';
import {ActivatedRoute, Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {RouterTestingModule} from '@angular/router/testing';
import {ApiService} from '../../../services/api.service';
import {SnackbarService} from '../../../services/snackbar.service';
import {NO_ERRORS_SCHEMA} from '@angular/core';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RegisterComponent ],
      imports: [RouterTestingModule],
      providers: [
        {provide: ApiService, useValue: {registerClient: () => {}, checkUsername: () => {}}},
        {provide: SnackbarService, useValue: {warning: () => {}, success: () => {}}},
        {provide: ActivatedRoute, useValue: {
          snapshot: {
            paramMap: {
              get: (param) => ''
            }
          }
          }},
        {provide: MatDialog, useValue: {}}
      ],
      schemas: [NO_ERRORS_SCHEMA]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
