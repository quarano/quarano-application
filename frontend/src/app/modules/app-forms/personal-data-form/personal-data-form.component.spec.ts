/* tslint:disable:no-unused-variable */
import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {PersonalDataFormComponent} from './personal-data-form.component';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {VALIDATION_PATTERNS} from '@utils/validation';

describe('PersonalDataFormComponent', () => {
  let component: PersonalDataFormComponent;
  let fixture: ComponentFixture<PersonalDataFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [PersonalDataFormComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PersonalDataFormComponent);
    component = fixture.componentInstance;

    component.formGroup = new FormGroup({
      firstName: new FormControl('', [Validators.required, Validators.pattern(VALIDATION_PATTERNS.name)]),
      lastName: new FormControl('', [Validators.required, Validators.pattern(VALIDATION_PATTERNS.name)]),
      email: new FormControl('', [Validators.required, Validators.pattern(VALIDATION_PATTERNS.email)]),
      phone: new FormControl('',
        [Validators.minLength(5), Validators.maxLength(17), Validators.pattern(VALIDATION_PATTERNS.phoneNumber)]),
      mobilePhone: new FormControl('',
        [Validators.minLength(5), Validators.maxLength(17), Validators.pattern(VALIDATION_PATTERNS.phoneNumber)]),
      street: new FormControl('', [Validators.required, Validators.pattern(VALIDATION_PATTERNS.street)]),
      houseNumber: new FormControl('', [Validators.pattern(VALIDATION_PATTERNS.houseNumber)]),
      zipCode: new FormControl('',
        [Validators.required, Validators.minLength(5), Validators.maxLength(5), Validators.pattern(VALIDATION_PATTERNS.zip)]),
      city: new FormControl('', [Validators.required]),
      dateOfBirth: new FormControl(new Date(), [Validators.required])
    });
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
