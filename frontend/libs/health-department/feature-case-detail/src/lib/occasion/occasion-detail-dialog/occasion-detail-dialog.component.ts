import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { OccasionDto } from '../../../../../domain/src/lib/model/occasion';

@Component({
  selector: 'qro-occasion-detail-dialog',
  templateUrl: './occasion-detail-dialog.component.html',
  styleUrls: ['./occasion-detail-dialog.component.scss'],
})
export class OccasionDetailDialogComponent {
  occasionFormGroup: FormGroup;
  initialOccasion: OccasionDto;

  @Input()
  occasion(occasion: OccasionDto) {
    console.log('initalOccasion', occasion);
    this.initialOccasion = occasion;
    this.mapOccasionToForm();
  }

  constructor(public dialogRef: MatDialogRef<OccasionDetailDialogComponent>, private builder: FormBuilder) {
    this.initializeEventForm();
    this.occasionFormGroup.valueChanges.subscribe((value) => {
      console.log(value);
    });
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
      title: this.occasionFormGroup?.controls?.title?.value,
      additionalInformation: this.occasionFormGroup?.controls?.additionalInformation?.value,
      end: this.occasionFormGroup?.controls?.end?.value,
      start: this.occasionFormGroup?.controls?.start?.value,
      houseNumber: this.occasionFormGroup?.controls?.houseNumber?.value,
      street: this.occasionFormGroup?.controls?.street?.value,
      zipCode: this.occasionFormGroup?.controls?.zipCode?.value,
      city: this.occasionFormGroup?.controls?.city?.value,
      contactPerson: this.occasionFormGroup?.controls?.contactPerson?.value,
      // properties set by backend
      occasionCode: this.initialOccasion?.occasionCode,
      trackedCaseId: this.initialOccasion?.trackedCaseId,
      visitorGroups: this.initialOccasion?.visitorGroups,
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
      visitorGroups: this.initialOccasion?.visitorGroups,
      contactPerson: this.initialOccasion?.contactPerson,
    };
    return (this.occasionFormGroup = this.builder.group(initialData));
  }
}
