import { FormBuilder } from '@angular/forms';
/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { AccountEditComponent } from './account-edit.component';
import { RouterTestingModule } from '@angular/router/testing';
import { ApiService } from '@services/api.service';
import { SnackbarService } from '@services/snackbar.service';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

describe('AccountEditComponent', () => {
  let component: AccountEditComponent;
  let fixture: ComponentFixture<AccountEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [AccountEditComponent],
      providers: [
        FormBuilder,
        { provide: ApiService, useValue: jasmine.createSpyObj(['createDiaryEntry']) },
        { provide: SnackbarService, useValue: jasmine.createSpyObj(['warning', 'success']) },
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
      ]
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
