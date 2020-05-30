/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderRightComponent } from './header-right.component';
import { of } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { UserService } from '../../../../../../../libs/auth/domain/src/lib/services/user.service';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';

describe('HeaderRightComponent', () => {
  let component: HeaderRightComponent;
  let fixture: ComponentFixture<HeaderRightComponent>;

  beforeEach(async(() => {
    const userService = {
      logout: () => {
      }
    } as any;
    userService.isLoggedIn$ = of();

    TestBed.configureTestingModule({
      imports: [MatMenuModule],
      declarations: [
        HeaderRightComponent],
      providers: [
        { provide: UserService, useValue: userService },
        { provide: MatDialog, useValue: {} }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderRightComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
