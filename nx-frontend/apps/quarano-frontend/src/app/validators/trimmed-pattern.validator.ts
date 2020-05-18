import {VALIDATION_PATTERNS, ValidationPattern} from './validation-patterns';
import { AbstractControl, Validators, ValidationErrors } from '@angular/forms';

export class TrimmedPatternValidator {

  public static trimmedPattern(pattern: VALIDATION_PATTERNS) {
    const validationPattern = ValidationPattern.validationPatterns.get(pattern);

    if (!validationPattern) { return Validators.nullValidator; }
    let regex: RegExp;
    let regexStr: string;
    if (typeof validationPattern.pattern === 'string') {
      regexStr = '';

      if (validationPattern.pattern.charAt(0) !== '^') { regexStr += '^'; }

      regexStr += validationPattern.pattern;

      if (validationPattern.pattern.charAt(validationPattern.pattern.length - 1) !== '$') { regexStr += '$'; }

      regex = new RegExp(regexStr);
    } else {
      regexStr = pattern.toString();
      regex = validationPattern.pattern;
    }
    return (control: AbstractControl): ValidationErrors | null => {
      const trimmedValue = control.value?.trim();
      if (isEmptyInputValue(trimmedValue)) {
        return null;
      }
      const value: string = trimmedValue;
      return regex.test(value) ? null :
        { trimmedPattern: { requiredPattern: regexStr, actualValue: value, errorMessage: validationPattern.errorMessage } };
    };
  }
}

function isEmptyInputValue(value: any): boolean {
  return value == null || value.length === 0;
}
