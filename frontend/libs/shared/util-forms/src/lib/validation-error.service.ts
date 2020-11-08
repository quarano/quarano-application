import { TranslateService } from '@ngx-translate/core';
import { DateFunctions } from '@qro/shared/util-date';
import { AbstractControl } from '@angular/forms';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { toUpper, snakeCase } from 'lodash';

@Injectable({ providedIn: 'root' })
export class ValidationErrorService {
  constructor(private translate: TranslateService) {}

  public getErrorKeys(control: AbstractControl): string[] {
    if (!control?.errors) {
      return null;
    }
    return Object.keys(control.errors);
  }

  // ToDo: E2E Test fixen
  // ToDo: Unit Tests fixen
  public getErrorMessage(control: AbstractControl, errorKey: string): Observable<string> {
    if (!control || control.valid) {
      return of('');
    }
    switch (errorKey) {
      case 'error400':
        return of(control.getError('error400').errorMessage);
      case 'trimmedPattern':
        return this.translate.get(control.getError('trimmedPattern').errorMessage);
      case 'minlength':
        return this.translate.get('VALIDATION.MINLENGTH', { value: control.getError('minlength').requiredLength });
      case 'maxlength':
        return this.translate.get('VALIDATION.MAXLENGTH', { value: control.getError('maxlength').requiredLength });
      case 'matDatepickerMax':
        return this.translate.get('VALIDATION.MAT_DATEPICKER_MAX', {
          value: DateFunctions.toCustomLocaleDateString(control.getError('matDatepickerMax').max.toDate()),
        });
      case 'matDatepickerMin':
        return this.translate.get('VALIDATION.MAT_DATEPICKER_MIN', {
          value: DateFunctions.toCustomLocaleDateString(control.getError('matDatepickerMin').min.toDate()),
        });
      default:
        return this.translate.get(`VALIDATION.${toUpper(snakeCase(errorKey))}`);
    }
  }
}
