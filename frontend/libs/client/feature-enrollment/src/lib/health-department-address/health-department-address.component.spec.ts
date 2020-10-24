/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { HealthDepartmentAddressComponent } from './health-department-address.component';

describe('HealthDepartmentAddressComponent', () => {
  let component: HealthDepartmentAddressComponent;
  let fixture: ComponentFixture<HealthDepartmentAddressComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [HealthDepartmentAddressComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HealthDepartmentAddressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
