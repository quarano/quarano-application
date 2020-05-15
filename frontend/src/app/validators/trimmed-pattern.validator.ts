import { FormControl, ValidationErrors, AbstractControl } from '@angular/forms';

export class TrimmedPatternValidator {

  public static match(pattern: string) {
    return (c: AbstractControl): { [key: string]: any } => {
      const trimmedValue = c.value?.trim();
      if (!trimmedValue) { return null; }
      if (trimmedValue.match(pattern)) {
        return null;
      }
      return { match: { valid: false } };
    };
  }
}
