import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'qro-event-new-dialog',
  templateUrl: './event-new-dialog.component.html',
  styleUrls: ['./event-new-dialog.component.scss'],
})
export class EventNewDialogComponent {
  occasionFormGroup: FormGroup;

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
    this.dialogRef.close(this.occasionFormGroup.getRawValue());
  }

  private initializeEventForm() {
    this.occasionFormGroup = this.builder.group(this.initialOccasion);
    this.occasionFormGroup.controls.title.setValidators([Validators.required]);
    this.occasionFormGroup.controls.start.setValidators([Validators.required]);
    this.occasionFormGroup.controls.end.setValidators([Validators.required]);
  }
}
