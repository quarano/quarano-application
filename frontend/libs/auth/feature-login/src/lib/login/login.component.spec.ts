import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { UserService } from '@qro/auth/domain';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';

fdescribe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async(() => {
    const userService = {
      login: () => {},
      isHealthDepartmentUser: () => {},
    } as any;
    userService.isLoggedIn$ = of();
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, HttpClientTestingModule, RouterTestingModule],
      declarations: [LoginComponent],
      providers: [
        { provide: UserService, useValue: userService },
        { provide: MatDialogRef, useValue: {} },
        { provide: MatDialog, useValue: {} },
        {
          provide: SnackbarService,
          useValue: {
            warning: () => {},
            success: () => {},
          },
        },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    });
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
