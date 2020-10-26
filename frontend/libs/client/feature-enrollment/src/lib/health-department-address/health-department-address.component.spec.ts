/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';

import { HealthDepartmentAddressComponent } from './health-department-address.component';
import { TranslateTestingModule } from '@qro/shared/util-translation';
import { RouterTestingModule } from '@angular/router/testing';

describe('HealthDepartmentAddressComponent', () => {
  let component: HealthDepartmentAddressComponent;
  let fixture: ComponentFixture<HealthDepartmentAddressComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [TranslateTestingModule, RouterTestingModule],
      declarations: [HealthDepartmentAddressComponent],
      schemas: [NO_ERRORS_SCHEMA],
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
