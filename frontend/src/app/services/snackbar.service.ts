import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class SnackbarService {
  duration = 3000;

  constructor(public snackbar: MatSnackBar) { }

  confirm(message: string): MatSnackBarRef<SimpleSnackBar> {
    return this.snackbar.open(message, 'Ok');
  }

  success(message: string) {
    const config = new MatSnackBarConfig();
    config.panelClass = ['background-green'];
    config.duration = this.duration;
    this.snackbar.open(message, null, config);
  }

  error(message: string) {
    const config = new MatSnackBarConfig();
    config.panelClass = ['background-red'];
    this.snackbar.open(message, 'x', config);
  }

  warning(message: string) {
    const config = new MatSnackBarConfig();
    config.panelClass = ['background-orange'];
    config.duration = this.duration;
    this.snackbar.open(message, null, config);
  }

  message(message: string) {
    const config = new MatSnackBarConfig();
    config.duration = this.duration;
    this.snackbar.open(message, null, config);
  }
}
