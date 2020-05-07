import { MatDialog } from '@angular/material/dialog';
import { Component, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges, EventEmitter } from '@angular/core';
import { FormControl, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { CaseDetailDto } from '@models/case-detail';
import { VALIDATION_PATTERNS } from '@utils/validation';
import { Subject } from 'rxjs';
import * as moment from 'moment';
import { SubSink } from 'subsink';
import { ClientType } from '@models/report-case';
import { ConfirmationDialogComponent } from '@ui/confirmation-dialog/confirmation-dialog.component';

const PhoneOrMobilePhoneValidator: ValidatorFn = (fg: FormGroup) => {
  const phone = fg.get('phone')?.value;
  const mobilePhone = fg.get('mobilePhone')?.value;
  return phone || mobilePhone ? null : { phoneMissing: true };
};


@Component({
  selector: 'app-client-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.scss']
})
export class EditComponent implements OnInit, OnChanges, OnDestroy {
  private subs: SubSink = new SubSink();
  ClientType = ClientType;
  today = new Date();

  @Input()
  caseDetail: CaseDetailDto;
  @Input()
  type: ClientType;
  @Output()
  changedToIndex = new EventEmitter<boolean>();

  formGroup: FormGroup;

  @Output()
  submittedValues: Subject<CaseDetailDto> = new Subject<CaseDetailDto>();

  constructor(private dialog: MatDialog) {
    this.createFormGroup();
  }

  ngOnInit(): void {
    this.subs.add(this.formGroup.get('quarantineStartDate').valueChanges.subscribe((value) => {
      this.formGroup.get('quarantineEndDate').setValue(moment(value).add(2, 'weeks'));
    }));
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.hasOwnProperty('caseDetail')) {
      this.updateFormGroup(this.caseDetail);
    }
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  createFormGroup() {
    this.formGroup = new FormGroup({
      firstName: new FormControl('', [Validators.required]),
      lastName: new FormControl('', [Validators.required]),

      testDate: new FormControl(this.type === ClientType.Index ? this.today : null),

      quarantineStartDate: new FormControl(this.type === ClientType.Index ? new Date() : null, []),
      quarantineEndDate: new FormControl(this.type === ClientType.Index ? moment().add(2, 'weeks').toDate() : null, []),

      street: new FormControl(''),
      houseNumber: new FormControl(''),
      city: new FormControl(''),
      zipCode: new FormControl('', [
        Validators.minLength(5), Validators.maxLength(5),
        Validators.pattern(VALIDATION_PATTERNS.integerUnsigned)]),

      mobilePhone: new FormControl('', [
        Validators.minLength(5), Validators.maxLength(17),
        Validators.pattern(VALIDATION_PATTERNS.phoneNumber)
      ]),
      phone: new FormControl('', [
        Validators.minLength(5), Validators.maxLength(17),
        Validators.pattern(VALIDATION_PATTERNS.phoneNumber)
      ]),

      email: new FormControl('', [Validators.pattern(VALIDATION_PATTERNS.email)]),

      dateOfBirth: new FormControl(null, []),

      comment: new FormControl('', []),

      infected: new FormControl(this.type === ClientType.Index ? true : false, []),
    });
    this.setValidators();
    this.formGroup.controls.testDate.valueChanges.subscribe(value => {
      if (value && this.type === ClientType.Contact) {
        this.onTestDateAdded();
      }
    });
  }

  onTestDateAdded() {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: {
        title: 'Zum Indexfall machen?',
        text:
          'Sind Sie sich sicher? Durch das Eintragen eines positiven Tests bearbeiten Sie den Kontaktfall ab sofort als Indexfall'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.changedToIndex.emit(true);
      } else {
        this.formGroup.controls.testDate.setValue(null);
      }
    });
  }

  setValidators() {
    if (this.type === ClientType.Index) {
      this.formGroup.setValidators([PhoneOrMobilePhoneValidator]);
    } else {
      this.formGroup.clearValidators();
    }
    this.formGroup.updateValueAndValidity();
  }

  updateFormGroup(caseDetailDto: CaseDetailDto) {
    if (caseDetailDto) {
      Object.keys(this.formGroup.value).forEach((key) => {
        if (caseDetailDto.hasOwnProperty(key)) {
          let value = caseDetailDto[key];
          if (RegExp(/\d{4}-\d{2}-\d{2}/).test(value)) {
            value = new Date(value);
          }
          this.formGroup.get(key).setValue(value);
        }
      });
    }
  }

  submitForm() {
    const submitData: any = { ...this.formGroup.value };

    Object.keys(submitData).forEach((key) => {
      if (moment.isMoment(submitData[key])) {
        submitData[key] = submitData[key].toDate();
      }
    });

    if (this.caseDetail?.caseId) {
      submitData.caseId = this.caseDetail.caseId;
    }

    this.submittedValues.next(submitData);
  }
}
