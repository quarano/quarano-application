import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { OccasionDto } from '../../../../../domain/src/lib/model/occasion';

@Component({
  selector: 'qro-event-new-dialog',
  templateUrl: './event-new-dialog.component.html',
  styleUrls: ['./event-new-dialog.component.scss'],
})
export class EventNewDialogComponent {
  occasionFormGroup: FormGroup;
  initialOccasion: OccasionDto;

  @Input()
  occasion(occasion: OccasionDto) {
    this.initialOccasion = occasion;
    this.mapOccasionToForm();
  }

  constructor(public dialogRef: MatDialogRef<EventNewDialogComponent>, private builder: FormBuilder) {
    this.initializeEventForm();
  }

  close() {
    this.dialogRef.close();
  }

  closeAndSubmit() {
    this.dialogRef.close(this.mapFormToOccasion());
  }

  private initializeEventForm() {
    this.mapOccasionToForm();
    this.occasionFormGroup.controls.title.setValidators([Validators.required]);
    this.occasionFormGroup.controls.start.setValidators([Validators.required]);
    this.occasionFormGroup.controls.end.setValidators([Validators.required]);
  }

  private mapFormToOccasion() {
    return {
      id: this.initialOccasion?.id ? this.initialOccasion?.id : null,
      occasionCode: this.initialOccasion?.occasionCode ? this.initialOccasion?.occasionCode : null,
      additionalInformation: this.occasionFormGroup?.controls?.additionalInformation?.value,
      end: this.occasionFormGroup?.controls?.end?.value,
      start: this.occasionFormGroup?.controls?.start?.value,
      title: this.occasionFormGroup?.controls?.title?.value,
      city: this.occasionFormGroup?.controls?.city?.value,
      houseNumber: this.occasionFormGroup?.controls?.houseNumber?.value,
      street: this.occasionFormGroup?.controls?.street?.value,
      zipCode: this.occasionFormGroup?.controls?.zipCode?.value,
      // contactPerson: null, // todo
      // trackedCaseId: null, // todo
      // visitorGroups: null // todo
    };
  }

  private mapOccasionToForm() {
    const initialData = {
      additionalInformation: this.initialOccasion?.additionalInformation,
      end: this.initialOccasion?.end,
      occasionCode: this.initialOccasion?.occasionCode,
      start: this.initialOccasion?.start,
      title: this.initialOccasion?.title,
      trackedCaseId: this.initialOccasion?.trackedCaseId,
      street: this.initialOccasion?.address?.street,
      houseNumber: this.initialOccasion?.address?.houseNumber,
      city: this.initialOccasion?.address?.city,
      zipCode: this.initialOccasion?.address?.zipCode,
      visitorGroups: [], //todo
      contactPerson: null, // todo
    };
    return (this.occasionFormGroup = this.builder.group(initialData));
  }
}
