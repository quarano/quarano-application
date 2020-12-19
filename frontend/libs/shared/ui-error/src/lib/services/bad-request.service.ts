import { IErrorToDisplay, HttpStatusCode } from './../interceptors/error.interceptor';
import { FormGroup } from '@angular/forms';
import { SnackbarService, TranslatedSnackbarService } from '@qro/shared/util-snackbar';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class BadRequestService {
  constructor(private translatedSnackbar: TranslatedSnackbarService, private snackbar: SnackbarService) {}

  public handleBadRequestError(error: any, form: FormGroup) {
    let handled = false;

    // remove ngrx/data error wrapper
    if (error.error) {
      error = error.error;
    }

    if (error.status === HttpStatusCode.badRequest.valueOf()) {
      let requestErrors = error.errors;
      if (requestErrors instanceof String || typeof requestErrors === 'string') {
        requestErrors = JSON.parse(requestErrors as string);
      }
      if (form) {
        Object.keys(form.controls).forEach((key) => {
          if (requestErrors.hasOwnProperty(key)) {
            handled = true;
            form.get(key).setErrors({ error400: { errorMessage: requestErrors[key] } });
          }
        });
      }

      if (!handled) {
        this.translatedSnackbar.error(requestErrors?.message || 'BAD_REQUEST.UNGÜLTIGE_WERTE').subscribe();
      }
    }

    if (error.status === HttpStatusCode.preconditionFailed.valueOf()) {
      this.snackbar.error(error.errors);
    }
  }
}
