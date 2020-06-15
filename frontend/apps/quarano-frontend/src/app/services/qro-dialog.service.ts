import { Injectable, TemplateRef } from '@angular/core';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { ConfirmationDialogComponent } from '@qro/shared/ui-confirmation-dialog';
import { ComponentType } from '@angular/cdk/portal';
import { Observable } from 'rxjs';
import { filter, switchMap } from 'rxjs/operators';
import { ContactPersonDialogComponent } from '@qro/client/ui-contact-person-detail';

export interface ContactPerson {
  id?: string;
  lastName?: string;
  firstName?: string;
  phone?: string;
  email?: string;
}

export interface ContactPersonDialogData {
  contactPerson?: ContactPerson;
}

export interface ConfirmDialogData {
  text?: string;
  title?: string;
  abortButtonText?: string;
  confirmButtonText?: string;
}

@Injectable({
  providedIn: 'root',
})
export class QroDialogService {
  constructor(private dialog: MatDialog) {}

  openContactPersonDialog(dialogConfig: MatDialogConfig = {}): MatDialogRef<any> {
    const standardConfig: MatDialogConfig = {
      height: '90vh',
      maxWidth: '100vw',
      data: {
        contactPerson: {
          id: null,
          lastName: null,
          firstName: null,
          phone: null,
          email: null,
        },
      },
    };
    const combinedConfig = { ...standardConfig, ...dialogConfig };
    return this.dialog.open(ContactPersonDialogComponent, combinedConfig);
  }

  openConfirmDialog(dialogConfig: MatDialogConfig = {}) {
    const standardConfig = {
      minWidth: '50vw',
      data: {
        text: 'Möchten Sie diese Aktion durchführen?',
        title: 'Bestätigung',
        abortButtonText: 'Abbrechen',
        confirmButtonText: 'ok',
      },
    };
    const combinedConfig = { ...standardConfig, ...dialogConfig };
    combinedConfig.data = { ...standardConfig.data, ...dialogConfig.data };
    return this.dialog.open(ConfirmationDialogComponent, combinedConfig);
  }

  open(componentOrTemplateRef: ComponentType<any> | TemplateRef<any>, config: MatDialogConfig) {
    return this.dialog.open(componentOrTemplateRef, config);
  }

  askAndOpenContactPersonDialog(name: string): Observable<any> {
    const dialogData: ConfirmDialogData = {
      text:
        'Sie haben einen Namen einer Kontaktperson angegeben, den Sie bisher noch nicht angelegt haben. ' +
        'Möchte Sie die Kontaktperson jetzt anlegen?',
      title: 'Neue Kontaktperson',
      abortButtonText: 'Abbrechen',
      confirmButtonText: 'Kontakt anlegen',
    };

    return this.openConfirmDialog({ data: dialogData })
      .afterClosed()
      .pipe(
        filter((choice) => choice),
        switchMap(() => {
          const firstName = name.split(' ')[0];
          const lastName = name.split(' ')[1];
          const contactPersonDialogData: ContactPersonDialogData = {
            contactPerson: {
              firstName: firstName,
              lastName: lastName,
            },
          };
          return this.openContactPersonDialog({ data: contactPersonDialogData }).afterClosed();
        })
      );
  }
}
