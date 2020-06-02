import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HdContactComponent } from './hd-contact.component';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('HdContactComponent', () => {
  let component: HdContactComponent;
  let fixture: ComponentFixture<HdContactComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [HdContactComponent],
      providers: [{ provide: MAT_DIALOG_DATA, useValue: {} }],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HdContactComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
