import { DateOrderValidator } from './date.validator';
import { ErrorStateMatcher } from '@angular/material/core';
import { FormControl, FormGroupDirective, NgForm } from '@angular/forms';
import { PasswordValidator } from './password.validator';

export class ConfirmValidPasswordMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    return !!PasswordValidator.mustMatch(control.parent) || (control.invalid && control.touched);
  }
}

export class PasswordIncludesUsernameMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    return !!PasswordValidator.mustNotIncludeUsername(control.parent) || (control.invalid && control.touched);
  }
}

export class DateOrderMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    return !!DateOrderValidator(control.parent) || (control.invalid && control.touched);
  }
}
