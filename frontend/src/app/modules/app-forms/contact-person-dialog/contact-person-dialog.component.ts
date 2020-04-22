import { ContactPersonDto } from '@models/contact-person';
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-contact-person-dialog',
  templateUrl: './contact-person-dialog.component.html',
  styleUrls: ['./contact-person-dialog.component.scss']
})
export class ContactPersonDialogComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA)
    public data: {
      contactPerson: ContactPersonDto;
    },
    private matDialogRef: MatDialogRef<ContactPersonDialogComponent>
  ) { }

  public close() {
    this.matDialogRef.close();
  }

  onContactCreated(createdContact: ContactPersonDto) {
    this.matDialogRef.close(createdContact);
  }
}
