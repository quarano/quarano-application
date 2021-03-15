import { LocationDialogComponent } from './../location-dialog/location-dialog.component';
import { Injectable } from '@angular/core';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';

@Injectable({
  providedIn: 'root',
})
export class LocationDialogService {
  constructor(private dialog: MatDialog) {}

  openLocationDialog(dialogConfig: MatDialogConfig = {}): MatDialogRef<any> {
    const standardConfig: MatDialogConfig = {
      height: '90vh',
      maxWidth: '100vw',
      data: {
        location: {
          id: null,
          name: null,
          contactPerson: { contactPersonName: null, contactPersonPhone: null, contactPersonEmail: null },
          street: null,
          houseNumber: null,
          zipCode: null,
          city: null,
          comment: null,
        },
      },
    };
    const combinedConfig = { ...standardConfig, ...dialogConfig };
    return this.dialog.open(LocationDialogComponent, combinedConfig);
  }
}
