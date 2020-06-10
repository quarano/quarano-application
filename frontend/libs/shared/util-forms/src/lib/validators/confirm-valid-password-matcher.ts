import { ErrorStateMatcher } from '@angular/material/core';
import { FormControl, FormGroupDirective, NgForm } from '@angular/forms';
import { PasswordValidator } from './password.validator';

export class ConfirmValidPasswordMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    return !!PasswordValidator.mustMatch(control.parent) || (control.invalid && control.touched);
  }
}
