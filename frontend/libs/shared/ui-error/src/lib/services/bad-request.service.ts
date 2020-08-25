import { FormGroup } from '@angular/forms';
import { TranslatedSnackbarService } from '@qro/shared/util-snackbar';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class BadRequestService {
  constructor(private snackbar: TranslatedSnackbarService) {}

  public handleBadRequestError(error: any, form: FormGroup) {
    let handled = false;
    if (error.hasOwnProperty('badRequestErrors')) {
      const requestErrors = error.badRequestErrors;
      Object.keys(form.controls).forEach((key) => {
        if (requestErrors.hasOwnProperty(key)) {
          handled = true;
          form.get(key).setErrors({ error400: { errorMessage: requestErrors[key] } });
        }
      });
      if (!handled) {
        this.snackbar.error('BAD_REQUEST.UNGÜLTIGE_WERTE').subscribe();
        console.log(requestErrors);
      }
    }
  }
}
