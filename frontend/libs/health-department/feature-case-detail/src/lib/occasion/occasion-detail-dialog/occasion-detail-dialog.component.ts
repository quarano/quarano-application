import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { OccasionDto } from '../../../../../domain/src/lib/model/occasion';
import { NgxMaterialTimepickerTheme } from 'ngx-material-timepicker';

@Component({
  selector: 'qro-occasion-detail-dialog',
  templateUrl: './occasion-detail-dialog.component.html',
  styleUrls: ['./occasion-detail-dialog.component.scss'],
})
export class OccasionDetailDialogComponent implements OnInit {
  initialOccasion: OccasionDto;
  occasionFormGroup: FormGroup;
  timepickerTheme: NgxMaterialTimepickerTheme;

  @Input()
  occasion(occasion: OccasionDto) {
    this.initialOccasion = occasion;
  }

  constructor(public dialogRef: MatDialogRef<OccasionDetailDialogComponent>, private builder: FormBuilder) {
    this.initialOccasion = {
      additionalInformation: '',
      address: undefined,
      contactPerson: '',
      end: undefined,
      occasionCode: '',
      start: undefined,
      title: '',
      trackedCaseId: '',
      visitorGroups: [],
    };
    this.occasionFormGroup = this.builder.group(this.mapOccasionToForm());
    this.timepickerTheme = {
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
  }

  ngOnInit() {
    this.occasionFormGroup = this.mapOccasionToForm();
    this.initializeValidators();
  }

  private mapTimestampToString(timestamp: Date) {
    if (!timestamp) {
      return '';
    }
    let timestampHour = timestamp?.getHours()?.toString();
    timestampHour === '0' ? (timestampHour = '00') : (timestampHour = '0');
    let timestampMinute = timestamp?.getMinutes()?.toString();
    timestampMinute === '0' ? (timestampMinute = '00') : (timestampMinute = '0');
    return timestampHour.concat(':').concat(timestampMinute);
  }

  setPickedStartTime(event: string) {
    this.occasionFormGroup.controls.timeStart.setValue(event);
  }

  setPickedEndTime(event: string) {
    this.occasionFormGroup.controls.timeEnd.setValue(event);
  }

  close() {
    this.dialogRef.close();
  }

  closeAndSubmit() {
    this.dialogRef.close(this.mapFormToOccasion());
  }

  private mapPickedDateAndTime(time: string, date: Date): Date {
    const splitted = time.split(':');
    const hour = splitted[0];
    const minute = splitted[1];

    if (!(date instanceof Date)) {
      // @ts-ignore
      date = (date as unknown).toDate();
    }

    hour ? date.setHours(Number(hour)) : date.setHours(0);
    minute ? date.setMinutes(Number(minute)) : date.setMinutes(0);
    return date;
  }

  private initializeValidators() {
    this.occasionFormGroup.controls.title.setValidators([Validators.required]);
    this.occasionFormGroup.controls.start.setValidators([Validators.required]);
    this.occasionFormGroup.controls.end.setValidators([Validators.required]);
    this.occasionFormGroup.controls.zipCode.setValidators([Validators.pattern('[0-9]{5}'), Validators.required]);
    this.occasionFormGroup.controls.timeStart.setValidators([Validators.pattern('((?:(?:0|1)\\d|2[0-3])):([0-5]\\d)')]);
    this.occasionFormGroup.controls.timeEnd.setValidators([Validators.pattern('((?:(?:0|1)\\d|2[0-3])):([0-5]\\d)')]);
  }

  private mapFormToOccasion() {
    const startTime = this.occasionFormGroup.controls.timeStart.value;
    const endTime = this.occasionFormGroup.controls.timeEnd.value;
    return {
      title: this.occasionFormGroup?.controls?.title?.value,
      additionalInformation: this.occasionFormGroup?.controls?.additionalInformation?.value,
      end: this.mapPickedDateAndTime(endTime, this.occasionFormGroup?.controls?.end?.value),
      start: this.mapPickedDateAndTime(startTime, this.occasionFormGroup?.controls?.start?.value),
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
      timeStart: this.mapTimestampToString(this.initialOccasion?.start),
      timeEnd: this.mapTimestampToString(this.initialOccasion?.end),
    };
    return this.builder.group(initialData);
  }
}
