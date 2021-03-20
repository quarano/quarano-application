import { Component, Input } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { OccasionDto } from '../../../../../domain/src/lib/model/occasion';
import { NgxMaterialTimepickerTheme } from 'ngx-material-timepicker';

@Component({
  selector: 'qro-occasion-detail-dialog',
  templateUrl: './occasion-detail-dialog.component.html',
  styleUrls: ['./occasion-detail-dialog.component.scss'],
})
export class OccasionDetailDialogComponent {
  occasionFormGroup: FormGroup;
  timeGroup: FormGroup;
  initialOccasion: OccasionDto;

  timepickerTheme: NgxMaterialTimepickerTheme = {
    container: {
      buttonColor: '#5ce1e6',
    },
    clockFace: {
      clockHandColor: '#5ce1e6',
    },
    dial: {
      dialBackgroundColor: '#5ce1e6',
    },
  };

  @Input()
  occasion(occasion: OccasionDto) {
    this.initialOccasion = occasion;
    this.initializeTimeForm();
    this.occasionFormGroup = this.mapOccasionToForm();
    this.initializeValidators();
    this.occasionFormGroup.controls.zipCode.valueChanges.subscribe((v) => console.log(v));
  }

  constructor(public dialogRef: MatDialogRef<OccasionDetailDialogComponent>, private builder: FormBuilder) {}

  prepareZeroHours(timeString: string) {
    return timeString === '0' ? '00' : timeString;
  }

  private initializeTimeForm() {
    const endHours = this.prepareZeroHours(this.initialOccasion?.end?.getHours().toString());
    const endMinutes = this.prepareZeroHours(this.initialOccasion?.end?.getMinutes().toString());
    const endTime = endHours.concat(':').concat(endMinutes);

    const startHours = this.prepareZeroHours(this.initialOccasion?.start?.getHours().toString());
    const startMinutes = this.prepareZeroHours(this.initialOccasion?.start?.getMinutes().toString());
    const startTime = startHours.concat(':').concat(startMinutes);

    this.timeGroup = new FormGroup({
      startTime: new FormControl(startTime, { validators: [Validators.pattern('((?:(?:0|1)\\d|2[0-3])):([0-5]\\d)')] }),
      endTime: new FormControl(endTime, { validators: [Validators.pattern('((?:(?:0|1)\\d|2[0-3])):([0-5]\\d)')] }),
    });
  }

  setStartTime(event: any) {
    this.timeGroup.controls.startTime.setValue(event);
  }

  setEndTime(event: any) {
    this.timeGroup.controls.endTime.setValue(event);
  }

  close() {
    this.dialogRef.close();
  }

  closeAndSubmit() {
    this.dialogRef.close(this.mapFormToOccasion());
  }

  private setDateTime(time: string, date: Date): Date {
    const splitted = time.split(':');
    const hour = splitted[0];
    const minute = splitted[1];
    if (hour || minute) {
      date.setHours(Number(hour));
      date.setMinutes(Number(minute));
    }
    return date;
  }

  private initializeValidators() {
    this.occasionFormGroup.controls.title.setValidators([Validators.required]);
    this.occasionFormGroup.controls.start.setValidators([Validators.required]);
    this.occasionFormGroup.controls.end.setValidators([Validators.required]);
    this.occasionFormGroup.controls.zipCode.setValidators([Validators.pattern('[0-9]{5}')]);
  }

  private mapFormToOccasion() {
    const startTime = this.timeGroup.controls.startTime.value;
    const endTime = this.timeGroup.controls.endTime.value;
    return {
      title: this.occasionFormGroup?.controls?.title?.value,
      additionalInformation: this.occasionFormGroup?.controls?.additionalInformation?.value,
      end: this.setDateTime(endTime, this.occasionFormGroup?.controls?.end?.value),
      start: this.setDateTime(startTime, this.occasionFormGroup?.controls?.start?.value),
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

  private mapOccasionToForm(): FormGroup {
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
    return this.builder.group(initialData);
  }
}
