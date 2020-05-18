import { FormBuilder } from '@angular/forms';
/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import {DebugElement, NO_ERRORS_SCHEMA} from '@angular/core';

import { AccountEditComponent } from './account-edit.component';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import {ApiService} from '../../../../services/api.service';
import {SnackbarService} from '../../../../services/snackbar.service';

describe('AccountEditComponent', () => {
  let component: AccountEditComponent;
  let fixture: ComponentFixture<AccountEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [AccountEditComponent],
      providers: [
        FormBuilder,
        { provide: ApiService, useValue: {} },
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
            snapshot: { paramMap: { get: (value) => '' } }
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
