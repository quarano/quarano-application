import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CloseCaseDialogComponent } from './close-case-dialog.component';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {NO_ERRORS_SCHEMA} from '@angular/core';

describe('CloseCaseDialogComponent', () => {
  let component: CloseCaseDialogComponent;
  let fixture: ComponentFixture<CloseCaseDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CloseCaseDialogComponent ],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: MatDialogRef, useValue: {} }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CloseCaseDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
