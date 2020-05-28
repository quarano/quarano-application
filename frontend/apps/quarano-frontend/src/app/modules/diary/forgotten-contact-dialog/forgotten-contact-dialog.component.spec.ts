/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ForgottenContactDialogComponent } from './forgotten-contact-dialog.component';
import { FormBuilder } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { EnrollmentService } from '../../../services/enrollment.service';
import { SnackbarService } from '@qro/shared/util';
import { NO_ERRORS_SCHEMA } from '@angular/core';


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
        {
          provide: EnrollmentService, useValue: {
            createEncounters: () => {
            }
          }
        },
        {
          provide: SnackbarService, useValue: {
            success: () => {
            }
          }
        },
        { provide: MatDialog, useValue: {} },
      ],
      schemas: [NO_ERRORS_SCHEMA]
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
