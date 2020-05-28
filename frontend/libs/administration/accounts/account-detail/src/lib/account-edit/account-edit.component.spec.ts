import { AccountService } from '@qro/administration/accounts/domain';
import { FormBuilder } from '@angular/forms';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { AccountEditComponent } from './account-edit.component';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { SnackbarService } from '@qro/shared/util';
import { AuthService } from '@qro/auth/api';

describe('AccountEditComponent', () => {
  let component: AccountEditComponent;
  let fixture: ComponentFixture<AccountEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [AccountEditComponent],
      providers: [
        FormBuilder,
        { provide: AccountService, useValue: {} },
        { provide: AuthService, useValue: {} },
        { provide: SnackbarService, useValue: {} },
        {
          provide: ActivatedRoute, useValue: {
            data: of({
              account: {
                accountId: null,
                firstName: null,
                lastName: null,
                username: null,
                _links: null,
                email: null,
                roles: []
              },
            }),
            snapshot: { paramMap: { get: () => '' } }
          }
        }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AccountEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
