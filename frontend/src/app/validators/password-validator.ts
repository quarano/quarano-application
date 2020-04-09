import {AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn} from '@angular/forms';

export class PasswordValidator {

  public static secure(control: FormControl): ValidationErrors {
    const password: string = control.value;
    const errors: ValidationErrors = {};

    if (!password) {
      return null;
    }

    if (password.length <= 7) {
      errors.minLength = true;
    }
    if (password.length > 30) {
      errors.maxLength = true;
    }
    if (!password.match(/(?=.*[A-Z])/)) {
      errors.uppercase = true;
    }
    if (!password.match(/(?=.*\d)/)) {
      errors.digit = true;
    }
    if (!password.match(/[!@#$%^&*(),.?:|<>]/)) {
      errors.nonWordChar = true;
    }

    return errors;
  }

  public static mustMatch(control: AbstractControl): { invalid: boolean } {
    if (!control.get('password').value || !control.get('confirmPassword').value) {
      return null;
    }

    if (control.get('password').value !== control.get('confirmPassword').value) {
      return {invalid: true};
    }
  }

  public static mustNotIncludeUsername(control: AbstractControl): { invalid: boolean } {
    if (!control.get('password').value || !control.get('username').value) {
      return null;
    }

    if (control.get('password').value.includes(control.get('username').value)) {
      return {invalid: true};
    }
  }
}
