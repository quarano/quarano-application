import { TrimmedPatternValidator, VALIDATION_PATTERNS } from '@qro/shared/util-form-validation';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PersonalDataFormComponent } from './personal-data-form.component';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NO_ERRORS_SCHEMA } from '@angular/core';

describe('PersonalDataFormComponent', () => {
  let component: PersonalDataFormComponent;
  let fixture: ComponentFixture<PersonalDataFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [PersonalDataFormComponent],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PersonalDataFormComponent);
    component = fixture.componentInstance;

    component.formGroup = new FormGroup({
      firstName: new FormControl('', [
        Validators.required,
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.name),
      ]),
      lastName: new FormControl('', [
        Validators.required,
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.name),
      ]),
      email: new FormControl('', [
        Validators.required,
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.email),
      ]),
      phone: new FormControl('', [
        Validators.minLength(5),
        Validators.maxLength(17),
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.phoneNumber),
      ]),
      mobilePhone: new FormControl('', [
        Validators.minLength(5),
        Validators.maxLength(17),
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.phoneNumber),
      ]),
      street: new FormControl('', [
        Validators.required,
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.street),
      ]),
      houseNumber: new FormControl('', [TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.houseNumber)]),
      zipCode: new FormControl('', [
        Validators.required,
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.zip),
      ]),
      city: new FormControl('', [Validators.required]),
      dateOfBirth: new FormControl(new Date(), [Validators.required]),
    });
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
