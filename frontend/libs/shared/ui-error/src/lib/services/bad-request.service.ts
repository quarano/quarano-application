import { IErrorToDisplay, HttpStatusCode } from './../interceptors/error.interceptor';
import { FormGroup } from '@angular/forms';
import { SnackbarService, TranslatedSnackbarService } from '@qro/shared/util-snackbar';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class BadRequestService {
  constructor(private translatedSnackbar: TranslatedSnackbarService, private snackbar: SnackbarService) {}

  public handleBadRequestError(error: IErrorToDisplay, form: FormGroup) {
    let handled = false;
    if (error.status === HttpStatusCode.badRequest.valueOf()) {
      const requestErrors = error.errors;
      Object.keys(form.controls).forEach((key) => {
        if (requestErrors.hasOwnProperty(key)) {
          handled = true;
          form.get(key).setErrors({ error400: { errorMessage: requestErrors[key] } });
        }
      });
      if (!handled) {
        this.translatedSnackbar.error('BAD_REQUEST.UNGÜLTIGE_WERTE').subscribe();
      }
    }

    if (error.status === HttpStatusCode.preconditionFailed.valueOf()) {
      this.snackbar.error(error.errors);
    }
  }
}
