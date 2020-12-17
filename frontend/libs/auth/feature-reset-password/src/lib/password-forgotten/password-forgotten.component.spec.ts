import { RouterTestingModule } from '@angular/router/testing';
/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';

import { PasswordForgottenComponent } from './password-forgotten.component';
import { TranslateTestingModule } from '@qro/shared/util-translation';
import { AuthService } from '@qro/auth/domain';
import { TranslatedSnackbarService } from '@qro/shared/util-snackbar';
import { BadRequestService } from '@qro/shared/ui-error';
import { ValidationErrorService } from '@qro/shared/util-forms';

describe('PasswordForgottenComponent', () => {
  let component: PasswordForgottenComponent;
  let fixture: ComponentFixture<PasswordForgottenComponent>;

  beforeEach(
    waitForAsync(() => {
      TestBed.configureTestingModule({
        declarations: [PasswordForgottenComponent],
        schemas: [NO_ERRORS_SCHEMA],
        imports: [RouterTestingModule, TranslateTestingModule],
        providers: [
          { provide: AuthService, useValue: {} },
          { provide: TranslatedSnackbarService, useValue: { warning: () => {}, success: () => {} } },
          { provide: BadRequestService, useValue: {} },
          { provide: ValidationErrorService, useValue: { getErrorKeys: () => [] } },
        ],
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(PasswordForgottenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
