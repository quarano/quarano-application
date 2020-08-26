import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ContactPersonFormComponent } from './contact-person-form.component';
import { FormBuilder } from '@angular/forms';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ContactPersonService, ContactPersonDto } from '@qro/client/domain';

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

  it('should mark fields with truthy initial value as touched', () => {
    component.contactPerson = {
      firstName: 'Test',
      lastName: 'Meiser1',
    } as ContactPersonDto;
    component.ngOnInit();

    expect(component.formGroup.controls.firstName.touched).toBeTruthy();
    expect(component.formGroup.controls.lastName.touched).toBeTruthy();
  });

  it('should not mark fields with empty initial value as touched', () => {
    component.contactPerson = {
      firstName: 'Test',
      lastName: 'Meiser1',
    } as ContactPersonDto;
    component.ngOnInit();

    Object.keys(component.formGroup.controls).forEach((key: string) => {
      if (key !== 'firstName' && key !== 'lastName') {
        expect(component.formGroup.get(key).touched).toBeFalsy();
      }
    });
  });

  it('should validate fields with initial value', () => {
    component.contactPerson = {
      firstName: 'Test',
      lastName: 'Meiser1',
    } as ContactPersonDto;
    component.ngOnInit();

    expect(component.formGroup.controls.firstName.valid).toBeTruthy();
    expect(component.formGroup.controls.lastName.valid).toBeFalsy();
  });
});
