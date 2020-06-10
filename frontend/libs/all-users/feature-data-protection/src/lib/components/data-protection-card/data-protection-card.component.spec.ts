/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';

import { DataProtectionCardComponent } from './data-protection-card.component';

describe('DataProtectionComponent', () => {
  let component: DataProtectionCardComponent;
  let fixture: ComponentFixture<DataProtectionCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DataProtectionCardComponent],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DataProtectionCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
