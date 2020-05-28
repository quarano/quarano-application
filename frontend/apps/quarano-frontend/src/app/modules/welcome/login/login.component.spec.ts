import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { SnackbarService } from '@qro/shared/util';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { EnrollmentService } from '../../../services/enrollment.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async(() => {
    const userService = {
      login: () => {
      }, isHealthDepartmentUser: () => {
      }
    } as any;
    userService.isLoggedIn$ = of();
    TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        HttpClientTestingModule,
        RouterTestingModule
      ],
      declarations: [LoginComponent],
      providers: [
        { provide: UserService, useValue: userService },
        {
          provide: SnackbarService, useValue: {
            warning: () => {
            }, success: () => {
            }
          }
        },
        {
          provide: EnrollmentService, useValue: {}
        }
      ],
      schemas: [NO_ERRORS_SCHEMA]
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
