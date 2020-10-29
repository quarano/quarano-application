import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ContactListComponent } from './contact-list.component';
import { RouterTestingModule } from '@angular/router/testing';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { HttpClientModule } from '@angular/common/http';
import { API_URL } from '@qro/shared/util-data-access';

describe('ContactListComponent', () => {
  let component: ContactListComponent;
  let fixture: ComponentFixture<ContactListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ContactListComponent],
      imports: [RouterTestingModule, HttpClientModule],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        { provide: SnackbarService, useValue: {} },
        { provide: API_URL, useValue: '' },
      ],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContactListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  xit('should create', () => {
    expect(component).toBeTruthy();
  });
});
