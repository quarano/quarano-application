import { TranslateTestingModule } from '@qro/shared/util-translation';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ForgottenContactDialogComponent } from './forgotten-contact-dialog.component';
import { FormBuilder } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { TranslatedSnackbarService } from '@qro/shared/util-snackbar';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { EnrollmentService } from '@qro/client/domain';
import { ValidationErrorService } from '@qro/shared/util-forms';

describe('ForgottenContactDialogComponent', () => {
  let component: ForgottenContactDialogComponent;
  let fixture: ComponentFixture<ForgottenContactDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [TranslateTestingModule],
      declarations: [ForgottenContactDialogComponent],
      providers: [
        FormBuilder,
        { provide: MatDialogRef, useValue: {} },
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: ValidationErrorService, userValue: {} },
        {
          provide: EnrollmentService,
          useValue: {
            createEncounters: () => {},
          },
        },
        {
          provide: TranslatedSnackbarService,
          useValue: {
            success: () => {},
          },
        },
        { provide: MatDialog, useValue: {} },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
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
