import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { PasswordResetComponent } from './password-reset.component';
import { RouterTestingModule } from '@angular/router/testing';
import { TranslateTestingModule } from '@qro/shared/util-translation';
import { AuthService } from '@qro/auth/domain';
import { TranslatedSnackbarService } from '@qro/shared/util-snackbar';
import { BadRequestService } from '@qro/shared/ui-error';
import { ValidationErrorService } from '@qro/shared/util-forms';
import { ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { of } from 'rxjs';

describe('PasswordResetComponent', () => {
  let component: PasswordResetComponent;
  let fixture: ComponentFixture<PasswordResetComponent>;

  beforeEach(
    waitForAsync(() => {
      TestBed.configureTestingModule({
        declarations: [PasswordResetComponent],
        schemas: [NO_ERRORS_SCHEMA],
        imports: [ReactiveFormsModule, RouterTestingModule, TranslateTestingModule],
        providers: [
          { provide: AuthService, useValue: {} },
          { provide: TranslatedSnackbarService, useValue: { warning: () => {}, success: () => {} } },
          { provide: BadRequestService, useValue: {} },
          { provide: ValidationErrorService, useValue: { getErrorKeys: () => [] } },
          {
            provide: ActivatedRoute,
            useValue: {
              paramMap: of({}),
            },
          },
        ],
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(PasswordResetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
