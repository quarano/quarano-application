import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'qro-event-new-dialog',
  templateUrl: './event-new-dialog.component.html',
  styleUrls: ['./event-new-dialog.component.scss'],
})
export class EventNewDialogComponent {
  occasionFormGroup: FormGroup;
  addressFormGroup: FormGroup;

  initialOccasion = {
    additionalInformation: '',
    contactPerson: '',
    end: undefined,
    occasionCode: '',
    start: undefined,
    title: '',
    trackedCaseId: '',
    visitorGroups: '',
    street: '',
    houseNumber: undefined,
    city: '',
    zipCode: undefined,
  };

  constructor(public dialogRef: MatDialogRef<EventNewDialogComponent>, private builder: FormBuilder) {
    this.initializeEventForm();
  }

  close() {
    this.dialogRef.close();
  }

  closeAndSubmit() {
    console.log(this.occasionFormGroup.getRawValue());
    this.dialogRef.close(this.occasionFormGroup.getRawValue());
  }

  private initializeEventForm() {
    this.occasionFormGroup = this.builder.group(this.initialOccasion);
  }
}
