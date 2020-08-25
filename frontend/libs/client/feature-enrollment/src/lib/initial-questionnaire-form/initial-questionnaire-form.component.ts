import { TranslateService } from '@ngx-translate/core';
import {
  ValidationErrorService,
  VALIDATION_PATTERNS,
  TrimmedPatternValidator,
  ArrayValidator,
} from '@qro/shared/util-forms';
import { Component, Input, OnInit } from '@angular/core';
import { FormGroup, Validators, ValidatorFn } from '@angular/forms';
import { SubSink } from 'subsink';
import { SymptomDto } from '@qro/shared/util-symptom';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'qro-initial-questionnaire-form',
  templateUrl: './initial-questionnaire-form.component.html',
  styleUrls: ['./initial-questionnaire-form.component.scss'],
})
export class InitialQuestionnaireFormComponent implements OnInit {
  today = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());

  @Input()
  formGroup: FormGroup;

  @Input()
  symptoms: SymptomDto[];

  subs = new SubSink();

  symptomTooltip$: Observable<string>;

  constructor(private translate: TranslateService, public validationErrorService: ValidationErrorService) {}

  ngOnInit(): void {
    this.symptomTooltip$ = this.translate
      .get('INITIAL_QUESTIONNAIRE_FORM.CHARAKTERISTISCHE_SYMPTOME')
      .pipe(map((res) => `${res}: ${this.symptoms?.map((s) => s.name).join(', ')}`));
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
