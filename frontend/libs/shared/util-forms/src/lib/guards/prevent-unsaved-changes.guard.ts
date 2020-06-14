import { Injectable } from '@angular/core';
import { CanDeactivate } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ConfirmationDialogComponent } from '@qro/shared/ui-confirmation-dialog';

export interface DeactivatableComponent {
  canDeactivate: () => boolean | Observable<boolean>;
}

@Injectable({
  providedIn: 'root',
})
export class PreventUnsavedChangesGuard implements CanDeactivate<DeactivatableComponent> {
  constructor(public dialog: QroDialogService) {}

  canDeactivate(component: DeactivatableComponent) {
    return component.canDeactivate() ? true : this.confirmProceeding();
  }

  confirmProceeding(): Observable<boolean> {
    const data: ConfirmDialogData = {
      title: 'Fortfahren?',
      text:
        'Sind Sie sicher, dass Sie fortfahren möchten? Alle ungespeicherten Änderungen werden dabei verloren gehen.',
    };
    return this.dialog
      .openConfirmDialog({ data: data })
      .afterClosed()
      .pipe(
        map((result) => {
          return !!result;
        })
      );
  }
}
