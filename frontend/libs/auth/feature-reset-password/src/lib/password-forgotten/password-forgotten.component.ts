import { TrimmedPatternValidator, ValidationErrorService, VALIDATION_PATTERNS } from '@qro/shared/util-forms';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatInput } from '@angular/material/input';

@Component({
  selector: 'qro-password-forgotten',
  templateUrl: './password-forgotten.component.html',
  styleUrls: ['./password-forgotten.component.scss'],
})
export class PasswordForgottenComponent implements OnInit {
  formGroup: FormGroup;
  loading = false;

  constructor(public validationErrorService: ValidationErrorService, private formBuilder: FormBuilder) {}

  ngOnInit() {
    this.createFormGroup();
  }

  createFormGroup() {
    this.formGroup = this.formBuilder.group({
      username: new FormControl(null, [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(30),
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.username),
      ]),
      email: new FormControl(null, [TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.email)]),
    });
  }

  trimValue(input: MatInput) {
    input.value = input.value.trim();
  }

  submitForm() {}
}
