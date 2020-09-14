import { ActivatedRoute, Router } from '@angular/router';
import { distinctUntilChanged, filter, map, shareReplay, tap } from 'rxjs/operators';
import {
  PhoneOrMobilePhoneValidator,
  TrimmedPatternValidator,
  ValidationErrorService,
  VALIDATION_PATTERNS,
} from '@qro/shared/util-forms';
import { MatDialog } from '@angular/material/dialog';
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, NgForm, Validators } from '@angular/forms';
import { BehaviorSubject, combineLatest, Observable } from 'rxjs';
import * as moment from 'moment';
import { SubSink } from 'subsink';
import { MatInput } from '@angular/material/input';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { ConfirmationDialogComponent } from '@qro/shared/ui-confirmation-dialog';
import {
  CaseDto,
  CaseEntityService,
  CaseSearchItem,
  IndexCaseService,
  mapCaseIdToCaseEntity,
} from '@qro/health-department/domain';
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
export class EditComponent implements OnInit, OnDestroy {
  private subs: SubSink = new SubSink();
  today = new Date();
  selectableIndexCases: CaseSearchItem[] = [];
  type$$ = new BehaviorSubject<CaseType>(CaseType.Index);

  get isIndexCase() {
    return this.type$$.value === CaseType.Index;
  }

  caseDetail$: Observable<CaseDto>;
  loading$: Observable<boolean>;

  formGroup: FormGroup;
  @ViewChild('editForm') editFormElement: NgForm;

  constructor(
    private dialog: MatDialog,
    private snackbarService: SnackbarService,
    public indexCaseService: IndexCaseService,
    private route: ActivatedRoute,
    private router: Router,
    private entityService: CaseEntityService,
    public validationErrorService: ValidationErrorService
  ) {}

  ngOnInit(): void {
    this.createFormGroup();

    this.route.parent.paramMap.pipe(map((paramMap) => paramMap.get('type'))).subscribe((type) => {
      if (type === CaseType.Index) {
        this.type$$.next(CaseType.Index);
      } else if (type === CaseType.Contact) {
        this.type$$.next(CaseType.Contact);
      }
    });

    this.caseDetail$ = combineLatest([
      this.route.parent.paramMap.pipe(map((paramMap) => paramMap.get('id'))),
      this.entityService.entityMap$,
    ]).pipe(
      mapCaseIdToCaseEntity(),
      shareReplay(1),
      tap((data) => this.updateFormGroup(data)),
      filter(() => !!this.formGroup),
      tap(() => this.setValidators()),
      tap((data) => (data.infected === false && this.isIndexCase ? this.triggerErrorMessages() : undefined))
    );

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

    this.loading$ = this.entityService.loading$;
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
      infected: new FormControl(),

      extReferenceNumber: new FormControl('', [
        Validators.maxLength(40),
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.extReferenceNumber),
      ]),
      originCases: new FormControl([]),
      caseId: new FormControl(null),
    });
    this.setValidators();
    this.subs.add(
      this.formGroup.get('infected').valueChanges.subscribe((value) => {
        if (value && this.type$$.value === CaseType.Contact) {
          this.onTestDateAdded(this.formGroup.controls.caseId.value);
        }
      })
    );
  }

  onIndexCaseSearch(searchTerm: string) {
    this.indexCaseService.searchCases(searchTerm).subscribe((result) => (this.selectableIndexCases = [...result]));
  }

  onTestDateAdded(caseId: string) {
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
        this.router.navigate([`/health-department/case-detail/index/${caseId}`]);
      } else {
        this.formGroup.get('testDate').setValue(null);
      }
    });
  }

  showIndexCaseItem(item: CaseDto): string {
    if (!item) return '';
    return `${item.lastName}, ${item.firstName} (${
      item.dateOfBirth ? DateFunctions.toCustomLocaleDateString(item.dateOfBirth) : 'Geburtstag unbekannt'
    })`;
  }

  setValidators() {
    if (this.isIndexCase) {
      this.formGroup.setValidators([PhoneOrMobilePhoneValidator]);
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
      this.formGroup.controls.originCases.setValue(caseDetailDto?._embedded?.originCases);
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
      submitData.caseType = this.type$$.value;
      submitData.originCases = this.formGroup.controls.originCases.value.map((v: CaseSearchItem) => v._links.self.href);
      this.saveCaseData(submitData, closeAfterSave);
    }
  }

  saveCaseData(result: CaseDto, closeAfterSave: boolean) {
    if (!result.caseId) {
      this.caseDetail$ = this.entityService.add(result);
    } else {
      this.caseDetail$ = this.entityService.update(result);
    }

    this.subs.sink = this.caseDetail$.subscribe(() => {
      this.snackbarService.success('Persönliche Daten erfolgreich aktualisiert');
      if (closeAfterSave) {
        this.router.navigate([this.returnLink]);
      } else {
        this.formGroup.markAsPristine();
        this.formGroup.markAsUntouched();
      }
    });
  }

  get returnLink() {
    return `/health-department/${this.type$$.value}-cases/case-list`;
  }
}
