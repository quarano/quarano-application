import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InitialQuestionaireFormComponent } from './initial-questionaire-form.component';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {NO_ERRORS_SCHEMA} from '@angular/core';

describe('InitialQuestionaireFormComponent', () => {
  let component: InitialQuestionaireFormComponent;
  let fixture: ComponentFixture<InitialQuestionaireFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InitialQuestionaireFormComponent ],
      schemas: [NO_ERRORS_SCHEMA]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InitialQuestionaireFormComponent);
    component = fixture.componentInstance;

    component.formGroup = new FormGroup({
      hasSymptoms: new FormControl(null, [Validators.required]),
      dayOfFirstSymptoms: new FormControl(''),
      symptoms: new FormControl([]),
      familyDoctor: new FormControl('', []),
      guessedOriginOfInfection: new FormControl('', []),
      hasPreExistingConditions: new FormControl(null, [Validators.required]),
      hasPreExistingConditionsDescription: new FormControl(null),
      belongToMedicalStaff: new FormControl(null, [Validators.required]),
      belongToMedicalStaffDescription: new FormControl(null),
      hasContactToVulnerablePeople: new FormControl(null, [Validators.required]),
      hasContactToVulnerablePeopleDescription: new FormControl('')
    });
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
