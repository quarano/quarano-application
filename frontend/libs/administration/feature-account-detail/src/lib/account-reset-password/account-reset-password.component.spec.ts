import { AccountEntityService } from '@qro/administration/domain';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ApiService } from '@qro/shared/util-data-access';
import { BadRequestService } from '@qro/shared/ui-error';
import { RouterTestingModule } from '@angular/router/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { AccountResetPasswordComponent } from './account-reset-password.component';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { ValidationErrorService } from '@qro/shared/util-forms';
import { of } from 'rxjs';

describe('AccountResetPasswordComponent', () => {
  let component: AccountResetPasswordComponent;
  let fixture: ComponentFixture<AccountResetPasswordComponent>;

  beforeEach(
    waitForAsync(() => {
      TestBed.configureTestingModule({
        imports: [RouterTestingModule, FormsModule, ReactiveFormsModule],
        declarations: [AccountResetPasswordComponent],
        schemas: [NO_ERRORS_SCHEMA],
        providers: [
          { provide: SnackbarService, useValue: {} },
          { provide: ActivatedRoute, useValue: { parent: { queryParamMap: of({}) } } },
          { provide: ValidationErrorService, useValue: { getErrorKeys: () => [] } },
          { provide: BadRequestService, useValue: {} },
          { provide: ApiService, useValue: {} },
          { provide: AccountEntityService, useValue: {} },
        ],
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(AccountResetPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
