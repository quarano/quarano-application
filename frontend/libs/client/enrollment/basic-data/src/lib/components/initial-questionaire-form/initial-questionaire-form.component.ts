import {
  ValidationErrorGenerator,
  VALIDATION_PATTERNS,
  TrimmedPatternValidator,
  ArrayValidator,
} from '@qro/shared/util-form-validation';
import { Component, Input, OnInit } from '@angular/core';
import { FormGroup, Validators, ValidatorFn } from '@angular/forms';
import { SubSink } from 'subsink';
import { SymptomDto } from '@qro/shared/util-symptom';

@Component({
  selector: 'qro-initial-questionaire-form',
  templateUrl: './initial-questionaire-form.component.html',
  styleUrls: ['./initial-questionaire-form.component.scss'],
})
export class InitialQuestionaireFormComponent implements OnInit {
  today = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());

  @Input()
  formGroup: FormGroup;

  @Input()
  symptoms: SymptomDto[];

  subs = new SubSink();
  errorGenerator = ValidationErrorGenerator;

  tooltip =
    'In diesem Fall sind das folgende Vorerkankungen: chronische Herzerkrankung, ' +
    'Lungenerkrankungen (z. B. Asthma, COPD, chronische Bronchitis), chronische Lebererkrankungen, ' +
    'Diabetes mellitus (Zuckerkrankheit), Tumor-/Krebserkrankungen, Patienten mit geschwächtem ' +
    'Immunsystem (inkl. HIV/AIDS)';

  symptomTooltip: string;

  constructor() {}

  ngOnInit(): void {
    this.symptomTooltip = `Als Covid-19 charakterische Symptome zählen: ${this.symptoms
      ?.map((s) => s.name)
      .join(', ')}`;
    this.toggleAdditionalFieldValitations('hasSymptoms', 'dayOfFirstSymptoms', null, [Validators.required]);
    this.toggleAdditionalFieldValitations('hasSymptoms', 'symptoms', [], [ArrayValidator.minLengthArray(1)]);
    this.toggleAdditionalFieldValitations('belongToMedicalStaff', 'belongToMedicalStaffDescription', null, [
      Validators.required,
      TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.textual),
    ]);
    this.toggleAdditionalFieldValitations('hasPreExistingConditions', 'hasPreExistingConditionsDescription', null, [
      Validators.required,
      TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.textual),
    ]);
    this.toggleAdditionalFieldValitations(
      'hasContactToVulnerablePeople',
      'hasContactToVulnerablePeopleDescription',
      null,
      [Validators.required, TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.textual)]
    );
  }

  toggleAdditionalFieldValitations(trigger: string, field: string, emptyValue: any, validators: ValidatorFn[]) {
    this.subs.add(
      this.formGroup.get(trigger).valueChanges.subscribe((data) => {
        this.formGroup.get(field).setValidators(data ? validators : null);
        this.formGroup.get(field).updateValueAndValidity();
        if (!data) {
          this.formGroup.get(field).setValue(emptyValue);
        }
      })
    );
  }
}
