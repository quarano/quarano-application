import { AbstractControl, FormControl, ValidationErrors } from '@angular/forms';

export class PasswordValidator {

  public static secure(control: FormControl): ValidationErrors {
    const password: string = control.value?.trim();
    const errors: ValidationErrors = {};

    if (!password) {
      return null;
    }

    if (password.length <= 7) {
      errors.minlength = { 'requiredLength': 7, 'actualLength': password.length };
    }
    if (password.length > 500) {
      errors.maxlength = { 'requiredLength': 500, 'actualLength': password.length };
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
    if (!control.get('password').value?.trim() || !control.get('passwordConfirm').value?.trim()) {
      return null;
    }

    if (control.get('password').value?.trim() !== control.get('passwordConfirm').value?.trim()) {
      return { passwordConfirmWrong: true };
    }
  }

  public static mustNotIncludeUsername(control: AbstractControl): { passwordIncludesUsername: boolean } {
    if (!control.get('password').value?.trim() || !control.get('username').value?.trim()) {
      return null;
    }

    if (control.get('password').value.includes(control.get('username').value?.trim())) {
      return { passwordIncludesUsername: true };
    }
  }
}
