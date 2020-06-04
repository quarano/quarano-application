/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderLeftComponent } from './header-left.component';
import { of } from 'rxjs';
import { UserService } from '@qro/auth/api';
import { RouterTestingModule } from '@angular/router/testing';
import { EnrollmentService } from '@qro/client/enrollment/api';

describe('HeaderLeftComponent', () => {
  let component: HeaderLeftComponent;
  let fixture: ComponentFixture<HeaderLeftComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [HeaderLeftComponent],
      imports: [RouterTestingModule],
      providers: [
        { provide: UserService, useValue: { isLoggedIn$: of() } },
        { provide: EnrollmentService, useValue: {} },
      ],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderLeftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
