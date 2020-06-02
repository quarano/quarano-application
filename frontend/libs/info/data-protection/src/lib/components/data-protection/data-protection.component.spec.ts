/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';

import { DataProtectionComponent } from './data-protection.component';

describe('DataProtectionComponent', () => {
  let component: DataProtectionComponent;
  let fixture: ComponentFixture<DataProtectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DataProtectionComponent],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DataProtectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
