import {Component, Input, OnInit} from '@angular/core';
import {FormGroup, Validators} from '@angular/forms';
import {SubSink} from 'subsink';
import {SymptomDto} from '@models/symptom';

@Component({
  selector: 'app-initial-questionaire-form',
  templateUrl: './initial-questionaire-form.component.html',
  styleUrls: ['./initial-questionaire-form.component.scss']
})
export class InitialQuestionaireFormComponent implements OnInit {
  today = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());

  @Input()
  formGroup: FormGroup;

  @Input()
  symptoms: SymptomDto[];

  subs = new SubSink();

  tooltip = 'In diesem Fall sind das folgende Vorerkankungen: chronische Herzerkrankung, ' +
    'Lungenerkrankungen (z. B. Asthma, COPD, chronische Bronchitis), chronische Lebererkrankungen, ' +
    'Diabetes mellitus (Zuckerkrankheit), Tumor-/Krebserkrankungen, Patienten mit geschwächtem ' +
    'Immunsystem (inkl. HIV/AIDS)';

  constructor() {
  }

  ngOnInit(): void {
    this.toggleAdditionalFieldValitations('hasSymptoms', 'dayOfFirstSymptoms', null);
    this.toggleAdditionalFieldValitations('hasSymptoms', 'symptoms', []);

    this.toggleAdditionalFieldValitations('belongToMedicalStaff', 'belongToMedicalStaffDescription', null);

    this.toggleAdditionalFieldValitations('hasPreExistingConditions', 'hasPreExistingConditionsDescription', null);
    this.toggleAdditionalFieldValitations('hasContactToVulnerablePeople', 'hasContactToVulnerablePeopleDescription', null);
  }

  toggleAdditionalFieldValitations(trigger: string, field: string, emptyValue: any) {
    this.subs.add(this.formGroup.get(trigger).valueChanges.subscribe((data) => {
      this.formGroup.get(field).setValidators((data) ? Validators.required : null);
      this.formGroup.get(field).updateValueAndValidity();
      if (!data) {
        this.formGroup.get(field).setValue(emptyValue);
      }
    }));
  }

}
