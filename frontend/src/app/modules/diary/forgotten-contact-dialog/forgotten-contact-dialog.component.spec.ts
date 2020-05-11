/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { ForgottenContactDialogComponent } from './forgotten-contact-dialog.component';
import { FormBuilder } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { EnrollmentService } from '@services/enrollment.service';
import { SnackbarService } from '@services/snackbar.service';

describe('ForgottenContactDialogComponent', () => {
  let component: ForgottenContactDialogComponent;
  let fixture: ComponentFixture<ForgottenContactDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ForgottenContactDialogComponent],
      providers: [
        FormBuilder,
        { provide: MatDialogRef, useValue: {} },
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: EnrollmentService, useValue: jasmine.createSpyObj(['createEncounters']) },
        { provide: SnackbarService, useValue: jasmine.createSpyObj(['success']) },
        { provide: MatDialog, useValue: {} },
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ForgottenContactDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
