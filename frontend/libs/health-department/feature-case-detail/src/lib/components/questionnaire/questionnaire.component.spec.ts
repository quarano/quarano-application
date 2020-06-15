import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QuestionnaireComponent } from './questionnaire.component';

describe('QuestionnaireComponent', () => {
  let component: QuestionnaireComponent;
  let fixture: ComponentFixture<QuestionnaireComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [QuestionnaireComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QuestionnaireComponent);
    component = fixture.componentInstance;
    component.questionnaire = {
      hasSymptoms: false,
      symptoms: [],
      dayOfFirstSymptoms: '',
      familyDoctor: '',
      belongToMedicalStaff: false,
      belongToMedicalStaffDescription: '',
      guessedOriginOfInfection: '',
      hasContactToVulnerablePeople: false,
      hasContactToVulnerablePeopleDescription: '',
      hasPreExistingConditions: false,
      hasPreExistingConditionsDescription: '',
    };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
