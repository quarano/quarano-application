import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginComponent } from './login.component';
import { SnackbarService } from '@services/snackbar.service';
import { UserService } from '@services/user.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {Router} from '@angular/router';
import {of} from 'rxjs';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async(() => {
    const userService = jasmine.createSpyObj(['login', 'isHealthDepartmentUser']);
    userService.isLoggedIn$ = of();
    TestBed.configureTestingModule({
      providers: [
        { provide: UserService, useValue: userService },
        { provide: SnackbarService, useValue: jasmine.createSpyObj(['success']) },
        { provide: Router, useValue: jasmine.createSpyObj(['navigate']) }
      ]
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
