/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderLeftComponent } from './header-left.component';
import { of } from 'rxjs';
import { UserService } from '../../../../../auth/domain/src/lib/services/user.service';
import { RouterTestingModule } from '@angular/router/testing';

describe('HeaderLeftComponent', () => {
  let component: HeaderLeftComponent;
  let fixture: ComponentFixture<HeaderLeftComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [HeaderLeftComponent],
      imports: [RouterTestingModule],
      providers: [{ provide: UserService, useValue: { isLoggedIn$: of() } }],
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
