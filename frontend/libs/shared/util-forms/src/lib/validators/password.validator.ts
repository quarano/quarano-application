import { FormControl, ValidationErrors, FormGroup } from '@angular/forms';

export class PasswordValidator {
  public static secure(control: FormControl): ValidationErrors {
    const password: string = control?.value?.trim();
    const errors: ValidationErrors = {};

    if (!password) {
      return null;
    }

    if (password.length < 7) {
      errors.minlength = { requiredLength: 7, actualLength: password.length };
    }
    if (password.length > 500) {
      errors.maxlength = { requiredLength: 500, actualLength: password.length };
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

  public static mustMatch(formGroup: FormGroup): { passwordConfirmWrong: boolean } {
    if (!formGroup.get('password')) {
      throw new Error('mustMatch validator was set, but formGroup does not contain a control named password');
    }
    if (!formGroup.get('passwordConfirm')) {
      throw new Error('mustMatch validator was set, but formGroup does not contain a control named passwordConfirm');
    }
    if (!formGroup.get('password').value?.trim() || !formGroup.get('passwordConfirm').value?.trim()) {
      return null;
    }

    if (formGroup.get('password').value?.trim() !== formGroup.get('passwordConfirm').value?.trim()) {
      return { passwordConfirmWrong: true };
    }
  }

  public static mustNotIncludeUsername(formGroup: FormGroup): { passwordIncludesUsername: boolean } {
    if (!formGroup.get('username')) {
      throw new Error(
        'mustNotIncludeUsername validator was set, but formGroup does not contain a control named username'
      );
    }
    if (!formGroup.get('password')) {
      throw new Error(
        'mustNotIncludeUsername validator was set, but formGroup does not contain a control named password'
      );
    }

    if (!formGroup.get('password').value?.trim() || !formGroup.get('username').value?.trim()) {
      return null;
    }

    if (
      formGroup
        .get('password')
        .value?.trim()
        .toLowerCase()
        .includes(formGroup.get('username').value?.trim().toLowerCase())
    ) {
      return { passwordIncludesUsername: true };
    }
  }
}
