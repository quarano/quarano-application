import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { SubSink } from 'subsink';

@Component({
  selector: 'qro-event-new-dialog',
  templateUrl: './event-new-dialog.component.html',
  styleUrls: ['./event-new-dialog.component.scss'],
})
export class EventNewDialogComponent {
  eventFormGroup: FormGroup;

  constructor(public dialogRef: MatDialogRef<EventNewDialogComponent>) {
    this.initializeEventForm();
  }

  close() {
    this.dialogRef.close();
  }

  closeAndSubmit() {
    this.dialogRef.close(this.eventFormGroup.getRawValue());
  }

  private initializeEventForm() {
    this.eventFormGroup = new FormGroup({
      name: new FormControl(''),
      dateFrom: new FormControl(''),
      dateTo: new FormControl(''),
      address: new FormControl(''),
      status: new FormControl(''),
      participants: new FormControl(''),
      contactPerson: new FormControl(''),
      description: new FormControl(''),
      id: new FormControl(''),
    });
  }
}
