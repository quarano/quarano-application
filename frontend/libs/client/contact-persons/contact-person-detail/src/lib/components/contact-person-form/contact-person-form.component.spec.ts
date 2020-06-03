import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ContactPersonFormComponent } from './contact-person-form.component';
import { FormBuilder } from '@angular/forms';
import { SnackbarService } from '@qro/shared/util';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ContactPersonService, ContactPersonDto } from '@qro/client/contact-persons/domain';

describe('ContactPersonFormComponent', () => {
  let component: ContactPersonFormComponent;
  let fixture: ComponentFixture<ContactPersonFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ContactPersonFormComponent],
      providers: [
        FormBuilder,
        { provide: ContactPersonService, useValue: {} },
        { provide: SnackbarService, useValue: {} },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContactPersonFormComponent);
    component = fixture.componentInstance;
    component.contactPerson = {} as ContactPersonDto;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
