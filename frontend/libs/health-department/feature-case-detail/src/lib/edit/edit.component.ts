import { distinctUntilChanged } from 'rxjs/operators';
import {
  PhoneOrMobilePhoneValidator,
  TrimmedPatternValidator,
  VALIDATION_PATTERNS,
  ValidationErrorGenerator,
} from '@qro/shared/util-forms';
import { MatDialog } from '@angular/material/dialog';
import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  Output,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import { FormControl, FormGroup, NgForm, Validators } from '@angular/forms';
import { Subject } from 'rxjs';
import * as moment from 'moment';
import { SubSink } from 'subsink';
import { MatInput } from '@angular/material/input';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { ConfirmationDialogComponent } from '@qro/shared/ui-confirmation-dialog';
import { IndexCaseService, CaseListItemDto, CaseSearchItem, CaseDto } from '@qro/health-department/domain';
import { CaseType } from '@qro/auth/api';
import { DateFunctions } from '@qro/shared/util-date';

export interface CaseDetailResult {
  caseDetail: CaseDto;
  closeAfterSave: boolean;
}

@Component({
  selector: 'qro-client-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.scss'],
})
export class EditComponent implements OnInit, OnChanges, OnDestroy {
  private subs: SubSink = new SubSink();
  ClientType = CaseType;
  today = new Date();
  errorGenerator = ValidationErrorGenerator;
  selectableIndexCases: CaseSearchItem[] = [];

  get isIndexCase() {
    return this.type === CaseType.Index;
  }

  @Input()
  caseDetail: CaseDto;
  @Input()
  type: CaseType;
  @Input() loading: boolean;
  @Output()
  changedToIndex = new EventEmitter<boolean>();

  formGroup: FormGroup;
  @ViewChild('editForm') editFormElement: NgForm;

  @Output()
  submittedValues: Subject<CaseDetailResult> = new Subject<CaseDetailResult>();

  constructor(
    private dialog: MatDialog,
    private snackbarService: SnackbarService,
    public indexCaseService: IndexCaseService
  ) {}

  ngOnInit(): void {
    this.createFormGroup();

    this.updateFormGroup(this.caseDetail);

    this.subs.add(
      this.formGroup
        .get('quarantineStartDate')
        .valueChanges.pipe(distinctUntilChanged())
        .subscribe((value) => {
          if (value) {
            this.formGroup.get('quarantineEndDate').setValue(moment(value).add(2, 'weeks'));
          }
        })
    );
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.hasOwnProperty('caseDetail')) {
      this.updateFormGroup(this.caseDetail);
    }
    if ('type' in changes && this.formGroup) {
      this.setValidators();
      this.triggerErrorMessages();
    }
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  createFormGroup() {
    this.formGroup = new FormGroup({
      firstName: new FormControl('', [
        Validators.required,
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.name),
      ]),
      lastName: new FormControl('', [
        Validators.required,
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.name),
      ]),

      testDate: new FormControl(this.isIndexCase ? this.today : null),

      quarantineStartDate: new FormControl(this.isIndexCase ? new Date() : null, []),
      quarantineEndDate: new FormControl(this.isIndexCase ? moment().add(2, 'weeks').toDate() : null, []),

      street: new FormControl('', [TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.street)]),
      houseNumber: new FormControl('', [TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.houseNumber)]),
      city: new FormControl('', [TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.city)]),
      zipCode: new FormControl('', [TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.zip)]),

      mobilePhone: new FormControl('', [
        Validators.minLength(5),
        Validators.maxLength(17),
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.phoneNumber),
      ]),
      phone: new FormControl('', [
        Validators.minLength(5),
        Validators.maxLength(17),
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.phoneNumber),
      ]),

      email: new FormControl('', [TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.email)]),

      dateOfBirth: new FormControl(null, []),
      infected: new FormControl({ value: this.isIndexCase, disabled: this.isIndexCase }),

      extReferenceNumber: new FormControl('', [
        Validators.maxLength(40),
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.extReferenceNumber),
      ]),
      originCases: new FormControl(this.caseDetail?._embedded?.originCases || []),
    });
    this.setValidators();
    this.subs.add(
      this.formGroup.get('infected').valueChanges.subscribe((value) => {
        if (value && this.type === CaseType.Contact) {
          this.onTestDateAdded();
        }
      })
    );
  }

  onIndexCaseSearch(searchTerm: string) {
    this.indexCaseService.searchCases(searchTerm).subscribe((result) => (this.selectableIndexCases = [...result]));
  }

  onTestDateAdded() {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: {
        abortButtonText: 'Abbrechen',
        confirmButtonText: 'ok',
        title: 'Zum Indexfall machen?',
        text:
          'Sind Sie sich sicher? Durch das Eintragen eines positiven Tests bearbeiten Sie diese Kontaktperson ab sofort als Indexfall',
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.changedToIndex.emit(true);
      } else {
        this.formGroup.get('testDate').setValue(null);
      }
    });
  }

  showIndexCaseItem(item: CaseListItemDto): string {
    if (!item) return '';
    return `${item.firstName} ${item.lastName} (${
      item.dateOfBirth ? DateFunctions.toCustomLocaleDateString(item.dateOfBirth) : 'Geburtstag unbekannt'
    })`;
  }

  setValidators() {
    if (this.isIndexCase) {
      this.formGroup.setValidators([PhoneOrMobilePhoneValidator]);
      this.formGroup.get('infected').disable();
      this.formGroup.get('infected').setValue(true);
    } else {
      this.formGroup.clearValidators();
    }
    this.formGroup.updateValueAndValidity();
  }

  trimValue(input: MatInput) {
    input.value = input.value?.trim();
  }

  updateFormGroup(caseDetailDto: CaseDto) {
    if (caseDetailDto && caseDetailDto.caseId && this.formGroup) {
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

  private triggerErrorMessages() {
    this.snackbarService.confirm(
      'Um den Vorgang abzuschließen, bitte alle Pflichtfelder ausfüllen und auf "Speichern" klicken'
    );
    this.formGroup.markAsDirty();
    this.formGroup.markAllAsTouched();
    Object.keys(this.formGroup.controls).forEach((key) => {
      this.formGroup.controls[key].markAsDirty();
      this.formGroup.controls[key].updateValueAndValidity();
    });
    this.formGroup.updateValueAndValidity();
    setTimeout(() => this.editFormElement.ngSubmit.emit(), 0); // prevent premature submit
  }

  submitForm(form: NgForm, closeAfterSave: boolean) {
    if (this.formGroup.valid) {
      const submitData: CaseDto = { ...this.formGroup.getRawValue() };
      Object.keys(submitData).forEach((key) => {
        if (moment.isMoment(submitData[key])) {
          submitData[key] = submitData[key].toDate();
        }
      });
      if (this.caseDetail?.caseId) {
        submitData.caseId = this.caseDetail.caseId;
      }
      submitData.caseType = this.type;
      submitData.originCases = this.formGroup.controls.originCases.value.map((v: CaseSearchItem) => v._links.self.href);
      this.submittedValues.next({ caseDetail: submitData, closeAfterSave: closeAfterSave });
    }
  }

  get returnLink() {
    return `/health-department/${this.type}-cases/case-list`;
  }
}
