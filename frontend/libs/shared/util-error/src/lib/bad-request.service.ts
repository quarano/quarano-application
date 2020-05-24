import { FormGroup } from '@angular/forms';
import { SnackbarService } from './../../../../../apps/quarano-frontend/src/app/services/snackbar.service';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class BadRequestService {

  constructor(private snackbar: SnackbarService) { }

  public handleBadRequestError(error: any, form: FormGroup) {
    let handled = false;
    if (error.hasOwnProperty('badRequestErrors')) {
      const requestErrors = error.badRequestErrors;
      Object.keys(form.controls).forEach(key => {
        if (requestErrors.hasOwnProperty(key)) {
          handled = true;
          form.get(key).setErrors({ error400: { errorMessage: requestErrors[key] } });
        }
      });
      if (!handled) {
        this.snackbar.error('Die Aktion wurde wegen ungültiger Werte vom Server abgelehnt');
        console.log(error.BadRequestErrors);
      }
    }
  }
}
