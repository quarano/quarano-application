import { ValidationErrorGenerator } from '@qro/shared/util-form-validation';
import { FormGroup } from '@angular/forms';
import { Component, Input } from '@angular/core';
import { MatInput } from '@angular/material/input';

@Component({
  selector: 'qro-personal-data-form',
  templateUrl: './personal-data-form.component.html',
  styleUrls: ['./personal-data-form.component.scss']
})
export class PersonalDataFormComponent {
  today = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());
  @Input() formGroup: FormGroup;
  errorGenerator = ValidationErrorGenerator;

  trimValue(input: MatInput) {
    input.value = input.value.trim();
  }

}
