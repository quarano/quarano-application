import { AccountEntityService } from '@qro/administration/domain';
import { BadRequestService } from '@qro/shared/ui-error';
import { ValidationErrorService } from '@qro/shared/util-forms';
import { FormBuilder } from '@angular/forms';
import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { AccountEditComponent } from './account-edit.component';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { AuthService } from '@qro/auth/api';

describe('AccountEditComponent', () => {
  let component: AccountEditComponent;
  let fixture: ComponentFixture<AccountEditComponent>;

  beforeEach(
    waitForAsync(() => {
      TestBed.configureTestingModule({
        imports: [RouterTestingModule],
        declarations: [AccountEditComponent],
        providers: [
          FormBuilder,
          { provide: AccountEntityService, useValue: {} },
          { provide: AuthService, useValue: {} },
          { provide: SnackbarService, useValue: {} },
          { provide: BadRequestService, useValue: {} },
          { provide: ValidationErrorService, useValue: { getErrorKeys: () => [] } },
          { provide: ActivatedRoute, useValue: { parent: { paramMap: of({}) } } },
        ],
        schemas: [NO_ERRORS_SCHEMA],
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(AccountEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
