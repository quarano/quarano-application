import { Injectable } from '@angular/core';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { TranslatedConfirmationDialogComponent } from '@qro/shared/ui-confirmation-dialog';
import { Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { ContactPersonDialogComponent } from '..//contact-person-dialog/contact-person-dialog.component';
import { ContactLocationFormComponent } from '../contact-location-form/contact-location-form.component';

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
export class ContactDialogService {
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

  openContactLocationDialog(dialogConfig: MatDialogConfig = {}): MatDialogRef<ContactLocationFormComponent> {
    const standardConfig: MatDialogConfig = {
      height: '90vh',
      maxWidth: '100vw',
      data: {},
    };
    const combinedConfig = { ...standardConfig, ...dialogConfig };
    return this.dialog.open(ContactLocationFormComponent, combinedConfig);
  }

  askAndOpenContactPersonDialog(name: string): Observable<any> {
    return this.openConfirmDialog()
      .afterClosed()
      .pipe(
        flatMap((choice) => {
          if (choice) {
            const firstName = name.split(' ')[0];
            const lastName = name.split(' ')[1];
            const contactPersonDialogData: ContactPersonDialogData = {
              contactPerson: {
                firstName: firstName,
                lastName: lastName,
              },
            };
            return this.openContactPersonDialog({ data: contactPersonDialogData }).afterClosed();
          } else {
            // Notify the autocomplete field that no new contact was created
            return of(null);
          }
        })
      );
  }

  private openConfirmDialog() {
    const dialogData: ConfirmDialogData = {
      text: 'CONTACT_DIALOG.MÖCHTEN_SIE_DIE_KONTAKTPERSON_JETZT_ANLEGEN',
      title: 'CONTACT_DIALOG.NEUE_KONTAKTPERSON',
      abortButtonText: 'CONTACT_DIALOG.ABBRECHEN',
      confirmButtonText: 'CONTACT_DIALOG.KONTAKT_ANLEGEN',
    };

    return this.dialog.open(TranslatedConfirmationDialogComponent, { data: dialogData });
  }
}
