import { AbstractControl } from '@angular/forms';

export class ArrayValidator {
  public static minLengthArray(min: number) {
    return (c: AbstractControl): { [key: string]: any } => {
      if (c.value.length >= min) {
        return null;
      }
      return { minLengthArray: { valid: false } };
    };
  }
}
