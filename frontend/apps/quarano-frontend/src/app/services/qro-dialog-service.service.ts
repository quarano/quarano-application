import { Injectable, TemplateRef } from '@angular/core';
import { ContactPersonDialogComponent } from '../modules/app-forms/contact-person-dialog/contact-person-dialog.component';
import { DialogRole, MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { ConfirmationDialogComponent } from '@qro/shared/ui-confirmation-dialog';

export interface ContactPersonDialogData {
  contactPerson?: {
    id?: string;
    lastName?: string;
    firstName?: string;
    phone?: string;
    email?: string;
  };
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
export class QroDialogServiceService {
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
    return this.dialog.open(ConfirmationDialogComponent, combinedConfig);
  }

  open(component: TemplateRef<any>, dialogConfig: MatDialogConfig): MatDialogRef<any> {
    return this.dialog.open(component, dialogConfig);
  }
}
