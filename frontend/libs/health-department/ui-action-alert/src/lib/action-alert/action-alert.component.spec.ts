/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';

import { ActionAlertComponent } from './action-alert.component';
import { getAlertConfigurations } from '@qro/health-department/domain';

describe('ActionAlertComponent', () => {
  let component: ActionAlertComponent;
  let fixture: ComponentFixture<ActionAlertComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ActionAlertComponent],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActionAlertComponent);
    component = fixture.componentInstance;
    component.alert = getAlertConfigurations()[0];
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
