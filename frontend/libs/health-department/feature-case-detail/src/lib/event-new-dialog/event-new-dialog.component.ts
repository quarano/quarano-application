import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { SubSink } from 'subsink';

@Component({
  selector: 'qro-event-new-dialog',
  templateUrl: './event-new-dialog.component.html',
  styleUrls: ['./event-new-dialog.component.scss'],
})
export class EventNewDialogComponent implements OnInit {
  subs = new SubSink();

  eventFormGroup: FormGroup;

  constructor(public dialogRef: MatDialogRef<EventNewDialogComponent>) {
    this.subscribeToDialogCloseAndPassFormValueToParent();
    this.initializeEventForm();
  }

  private subscribeToDialogCloseAndPassFormValueToParent() {
    this.subs.add(
      this.dialogRef.beforeClosed().subscribe(() => {
        this.dialogRef.close(this.eventFormGroup.getRawValue());
      })
    );
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

  ngOnInit(): void {}
}
