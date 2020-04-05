import { Injectable } from '@angular/core';
import { CanDeactivate } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ConfirmationDialogComponent } from '../ui/confirmation-dialog/confirmation-dialog.component';

export interface DeactivatableComponent {
  canDeactivate: () => boolean | Observable<boolean>;
}

@Injectable({
  providedIn: 'root'
})
export class PreventUnsavedChangesGuard implements CanDeactivate<DeactivatableComponent> {
  constructor(public dialog: MatDialog) { }

  canDeactivate(component: DeactivatableComponent) {
    return component.canDeactivate() ? true : this.confirmProceeding();
  }

  confirmProceeding(): Observable<boolean> {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: {
        title: 'Fortfahren?',
        text:
          'Sind Sie sicher, dass Sie fortfahren möchten? Alle ungespeicherten Änderungen werden dabei verloren gehen.'
      }
    });

    return dialogRef.afterClosed().pipe(
      map(result => {
        return result ? true : false;
      })
    );
  }
}
