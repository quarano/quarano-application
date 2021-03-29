import { TranslateService } from '@ngx-translate/core';
import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class TranslatedSnackbarService {
  duration = 3000;

  constructor(public snackbar: MatSnackBar, private translate: TranslateService) {}

  confirm(messageKey: string): void {
    const config = new MatSnackBarConfig();
    config.panelClass = ['light-primary-color'];
    this.snackbar.open(this.translate.instant(messageKey), 'Ok', config);
  }

  success(messageKey: string, params?: { value: string }): void {
    const config = new MatSnackBarConfig();
    config.panelClass = ['background-green'];
    config.duration = this.duration;
    this.snackbar.open(this.translate.instant(messageKey, params), null, config);
  }

  error(messageKey: string): void {
    const config = new MatSnackBarConfig();
    config.panelClass = ['background-red'];
    this.snackbar.open(this.translate.instant(messageKey), 'x', config);
  }

  warning(messageKey: string): void {
    const config = new MatSnackBarConfig();
    config.panelClass = ['background-orange'];
    config.duration = this.duration;
    this.snackbar.open(this.translate.instant(messageKey), null, config);
  }

  message(messageKey: string): void {
    const config = new MatSnackBarConfig();
    config.duration = this.duration;
    config.panelClass = ['light-primary-color'];
    this.snackbar.open(this.translate.instant(messageKey), null, config);
  }
}
