import { AbstractControl, FormControl, ValidationErrors } from '@angular/forms';

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
    if (password.length > 500) {
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

  public static mustMatch(control: AbstractControl): { passwordConfirmWrong: boolean } {
    if (!control.get('password').value || !control.get('passwordConfirm').value) {
      return null;
    }

    if (control.get('password').value !== control.get('passwordConfirm').value) {
      return { passwordConfirmWrong: true };
    }
  }

  public static mustNotIncludeUsername(control: AbstractControl): { passwordIncludesUsername: boolean } {
    if (!control.get('password').value || !control.get('username').value) {
      return null;
    }

    if (control.get('password').value.includes(control.get('username').value)) {
      return { passwordIncludesUsername: true };
    }
  }
}
