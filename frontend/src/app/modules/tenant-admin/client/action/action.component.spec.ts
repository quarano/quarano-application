/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { ActionComponent } from './action.component';
import {MatDialog} from '@angular/material/dialog';
import {ApiService} from '@services/api.service';
import {SnackbarService} from '@services/snackbar.service';
import {FormBuilder} from '@angular/forms';
import {RouterTestingModule} from '@angular/router/testing';
import {CaseActionDto} from '@models/case-action';

describe('ActionComponent', () => {
  let component: ActionComponent;
  let fixture: ComponentFixture<ActionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [ ActionComponent ],
      providers: [
        FormBuilder,
        { provide: MatDialog, useValue: {}},
        {provide: ApiService, useValue: jasmine.createSpyObj(['resolveAnomalies'])},
        {provide: SnackbarService, useValue: jasmine.createSpyObj(['warning', 'success', 'message'])}
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActionComponent);
    component = fixture.componentInstance;
    component.caseAction = { anomalies: { health: [], process: [], resolved: [] } } as CaseActionDto;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
