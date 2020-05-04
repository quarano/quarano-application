import {Component, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges} from '@angular/core';
import {FormControl, FormGroup, ValidatorFn, Validators} from '@angular/forms';
import {CaseDetailDto} from '@models/case-detail';
import {VALIDATION_PATTERNS} from '@utils/validation';
import {Subject} from 'rxjs';
import * as moment from 'moment';
import {SubSink} from 'subsink';

const PhoneOrMobilePhoneValidator: ValidatorFn = (fg: FormGroup) => {
  const phone = fg.get('phone')?.value;
  const mobilePhone = fg.get('mobilePhone')?.value;
  return phone || mobilePhone ? null : {phoneMissing: true};
};


@Component({
  selector: 'app-client-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.scss']
})
export class EditComponent implements OnInit, OnChanges, OnDestroy {
  private subs: SubSink = new SubSink();

  today = new Date();

  @Input()
  caseDetail: CaseDetailDto;

  formGroup: FormGroup;

  @Output()
  submittedValues: Subject<CaseDetailDto> = new Subject<CaseDetailDto>();

  constructor() {
    this.formGroup = this.createFormGroup();
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
    return new FormGroup({
      firstName: new FormControl('', [Validators.required]),
      lastName: new FormControl('', [Validators.required]),

      testDate: new FormControl(this.today),

      quarantineStartDate: new FormControl(new Date(), []),
      quarantineEndDate: new FormControl(moment().add(2, 'weeks').toDate(), []),

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

      infected: new FormControl(true, []),
    }, [PhoneOrMobilePhoneValidator]);
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
    const submitData: any = {...this.formGroup.value};

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
