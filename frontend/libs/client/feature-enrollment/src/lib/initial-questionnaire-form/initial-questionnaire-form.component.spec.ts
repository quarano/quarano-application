import { TranslateTestingModule } from '@qro/shared/util-translation';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InitialQuestionnaireFormComponent } from './initial-questionnaire-form.component';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ValidationErrorService } from '@qro/shared/util-forms';

describe('InitialQuestionaireFormComponent', () => {
  let component: InitialQuestionnaireFormComponent;
  let fixture: ComponentFixture<InitialQuestionnaireFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [TranslateTestingModule],
      declarations: [InitialQuestionnaireFormComponent],
      providers: [{ provide: ValidationErrorService, useValue: { getErrorKeys: () => [] } }],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InitialQuestionnaireFormComponent);
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
      hasContactToVulnerablePeopleDescription: new FormControl(''),
    });
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
