import { Injectable } from '@angular/core';
import { CanDeactivate } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { TranslatedConfirmationDialogComponent } from '@qro/shared/ui-confirmation-dialog';

export interface DeactivatableComponent {
  canDeactivate: () => boolean | Observable<boolean>;
}

@Injectable({
  providedIn: 'root',
})
export class PreventUnsavedChangesGuard implements CanDeactivate<DeactivatableComponent> {
  constructor(public dialog: MatDialog) {}

  canDeactivate(component: DeactivatableComponent) {
    return component.canDeactivate() ? true : this.confirmProceeding();
  }

  confirmProceeding(): Observable<boolean> {
    const dialogRef = this.dialog.open(TranslatedConfirmationDialogComponent, {
      data: {
        title: 'PREVENT_UNSAVED_CHANGES.FORTFAHREN',
        abortButtonText: 'PREVENT_UNSAVED_CHANGES.ABBRECHEN',
        confirmButtonText: 'PREVENT_UNSAVED_CHANGES.OK',
        text: 'PREVENT_UNSAVED_CHANGES.SIND_SIE_SICHER',
      },
    });

    return dialogRef.afterClosed().pipe(
      map((result) => {
        return !!result;
      })
    );
  }
}
