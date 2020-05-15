import { AbstractControl, Validators, ValidationErrors } from '@angular/forms';

export class TrimmedPatternValidator {

  public static trimmedPattern(pattern: string | RegExp) {
    if (!pattern) { return Validators.nullValidator; }
    let regex: RegExp;
    let regexStr: string;
    if (typeof pattern === 'string') {
      regexStr = '';

      if (pattern.charAt(0) !== '^') { regexStr += '^'; }

      regexStr += pattern;

      if (pattern.charAt(pattern.length - 1) !== '$') { regexStr += '$'; }

      regex = new RegExp(regexStr);
    } else {
      regexStr = pattern.toString();
      regex = pattern;
    }
    return (control: AbstractControl): ValidationErrors | null => {
      const trimmedValue = control.value?.trim();
      if (isEmptyInputValue(trimmedValue)) {
        return null;
      }
      const value: string = trimmedValue;
      return regex.test(value) ? null :
        { trimmedPattern: { requiredPattern: regexStr, actualValue: value } };
    };
  }
}

function isEmptyInputValue(value: any): boolean {
  return value == null || value.length === 0;
}
