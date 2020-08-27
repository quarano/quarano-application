import { map, mergeMap, switchMap, tap } from 'rxjs/operators';
import { TranslateService } from '@ngx-translate/core';
import { BadRequestService } from '@qro/shared/ui-error';
import { DateFunctions } from '@qro/shared/util-date';
import { SubSink } from 'subsink';
import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { Observable } from 'rxjs';
import { ContactDialogService } from '@qro/client/ui-contact-person-detail';
import { ContactPersonDto, DiaryEntryDto, DiaryEntryModifyDto, DiaryService } from '@qro/client/domain';
import { SymptomDto } from '@qro/shared/util-symptom';
import { TranslatedSnackbarService } from '@qro/shared/util-snackbar';
import { DeactivatableComponent } from '@qro/shared/util-forms';

@Component({
  selector: 'qro-diary-entry',
  templateUrl: './diary-entry.component.html',
  styleUrls: ['./diary-entry.component.scss'],
})
export class DiaryEntryComponent implements OnInit, OnDestroy, DeactivatableComponent {
  formGroup: FormGroup;
  diaryEntry: DiaryEntryDto;
  nonCharacteristicSymptoms: SymptomDto[] = [];
  characteristicSymptoms: SymptomDto[] = [];
  contactPersons: ContactPersonDto[] = [];
  today = new Date();
  private subs = new SubSink();
  date: string;
  slot: string;
  loading = false;

  @HostListener('window:beforeunload')
  canDeactivate(): Observable<boolean> | boolean {
    return this.formGroup.pristine;
  }

  get isNew(): boolean {
    return this.diaryEntry?.id == null;
  }

  get isReadonly(): boolean {
    return !this.isNew && !this.diaryEntry._links.edit;
  }

  constructor(
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private diaryService: DiaryService,
    private snackbarService: TranslatedSnackbarService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private badRequestService: BadRequestService,
    private dialogService: ContactDialogService,
    private translate: TranslateService
  ) {}

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  ngOnInit() {
    this.date = this.activatedRoute.snapshot.paramMap.get('date');
    this.slot = this.activatedRoute.snapshot.paramMap.get('slot');
    this.subs.add(
      this.route.data.subscribe((data) => {
        this.diaryEntry = data.diaryEntry;
        // Fieber rausfiltern, da dies bereits über die Körpertemperatur abgefrühstückt wird
        this.setSymptoms(data.symptoms.filter((symptom) => symptom.id !== 'db723876-e051-4ccf-9c52-794190694666'));
        this.contactPersons = data.contactPersons;
        if (!this.isNew) {
          this.slot = this.diaryEntry.slot.timeOfDay;
        }
      })
    );
    this.buildForm();
  }

  setSymptoms(symptoms: SymptomDto[]) {
    symptoms.forEach((symptom) => {
      if (symptom.characteristic) {
        this.characteristicSymptoms.push(symptom);
      } else {
        this.nonCharacteristicSymptoms.push(symptom);
      }
    });
  }

  get nonCharacteristicSymptomIds() {
    return this.diaryEntry.nonCharacteristicSymptoms.map((s) => s.id);
  }

  buildForm() {
    const characteristicSymptomIds = this.diaryEntry.characteristicSymptoms.map((s) => s.id);
    const contactPersonIds = this.diaryEntry.contacts.map((c) => c.id);
    this.formGroup = this.formBuilder.group({
      bodyTemperature: new FormControl({ value: this.diaryEntry.bodyTemperature || 35, disabled: this.isReadonly }, [
        Validators.required,
        Validators.min(35.1),
        Validators.max(44.0),
      ]),
      characteristicSymptoms: new FormControl({ value: characteristicSymptomIds, disabled: this.isReadonly }),
      nonCharacteristicSymptoms: new FormControl({
        value: this.nonCharacteristicSymptomIds,
        disabled: this.isReadonly,
      }),
      dateTime: new FormControl({ value: this.diaryEntry.date, disabled: this.isReadonly }, Validators.required),
      contactPersons: new FormControl({ value: contactPersonIds, disabled: this.isReadonly }),
    });
  }

  onSubmit() {
    if (this.formGroup.valid) {
      this.loading = true;
      const diaryEntryModifyDto: DiaryEntryModifyDto = {
        id: null,
        bodyTemperature: null,
        symptoms: [],
        date: null,
        timeOfDay: null,
        contacts: [],
      };
      diaryEntryModifyDto.symptoms = this.characteristicSymptomsControl.value;
      diaryEntryModifyDto.id = this.diaryEntry.id;
      diaryEntryModifyDto.bodyTemperature = this.formGroup.controls.bodyTemperature.value;
      diaryEntryModifyDto.symptoms.push(...this.formGroup.controls.nonCharacteristicSymptoms.value);
      diaryEntryModifyDto.contacts = this.formGroup.controls.contactPersons.value;

      if (this.isNew) {
        this.createEntry(diaryEntryModifyDto);
      } else {
        this.modifyEntry(diaryEntryModifyDto);
      }
    }
  }

  getTitle(): Observable<string> {
    const slotKey = 'DIARY_ENTRY.' + (this.slot === 'morning' ? 'MORGENS' : 'ABENDS');
    if (this.isNew) {
      return this.translate.get('DIARY_ENTRY.TAGEBUCH_EINTRAG_ANLEGEN_FÜR_DEN').pipe(
        map((result) => `${result} ${DateFunctions.toCustomLocaleDateString(new Date(this.date))}`),
        switchMap((result) => this.translate.get(slotKey).pipe(map((res) => `${result} ${res}`)))
      );
    } else {
      return this.translate.get('DIARY_ENTRY.TAGEBUCH_EINTRAG_BEARBEITEN_FÜR_DEN').pipe(
        map((result) => `${result} ${DateFunctions.toCustomLocaleDateString(new Date(this.diaryEntry.slot.date))}`),
        switchMap((result) => this.translate.get(slotKey).pipe(map((res) => `${result} ${res}`)))
      );
    }
  }

  createEntry(diaryEntry: DiaryEntryModifyDto) {
    diaryEntry.date = this.date;
    diaryEntry.timeOfDay = this.slot;
    this.subs.add(
      this.diaryService
        .createDiaryEntry(diaryEntry)
        .pipe(switchMap(() => this.snackbarService.success('DIARY_ENTRY.TAGEBUCH_EINTRAG_ANGELEGT')))
        .subscribe(
          (_) => {
            this.formGroup.markAsPristine();
            this.router.navigate(['/client/diary/diary-list']);
          },
          (error) => {
            this.badRequestService.handleBadRequestError(error, this.formGroup);
          }
        )
        .add(() => (this.loading = false))
    );
  }

  modifyEntry(diaryEntry: DiaryEntryModifyDto) {
    diaryEntry.date = this.diaryEntry.slot.date;
    diaryEntry.timeOfDay = this.diaryEntry.slot.timeOfDay;
    this.subs.add(
      this.diaryService
        .modifyDiaryEntry(diaryEntry)
        .pipe(switchMap(() => this.snackbarService.success('DIARY_ENTRY.TAGEBUCH_EINTRAG_AKTUALISIERT')))
        .subscribe(
          (_) => {
            this.formGroup.markAsPristine();
            this.router.navigate(['/client/diary/diary-list']);
          },
          (error) => {
            this.badRequestService.handleBadRequestError(error, this.formGroup);
          }
        )
        .add(() => (this.loading = false))
    );
  }

  onSlideToggleChanged(event: MatSlideToggleChange, symptomId: string) {
    const control = this.characteristicSymptomsControl;
    let values = control.value as string[];

    if (event.checked) {
      values.push(symptomId);
    } else {
      values = values.filter((value) => value !== symptomId);
    }

    control.setValue(values);
    this.formGroup.markAsDirty();
  }

  get characteristicSymptomsControl() {
    return this.formGroup.controls.characteristicSymptoms;
  }

  isCharacteristicSymptomSelected(symptom: SymptomDto) {
    const selectedValues = this.characteristicSymptomsControl.value as string[];
    return selectedValues.includes(symptom.id);
  }

  formatLabel(value: number) {
    if (value === 0) {
      return '';
    }
    return value.toLocaleString();
  }

  openContactDialog() {
    this.dialogService
      .openContactPersonDialog()
      .afterClosed()
      .subscribe((createdContact: ContactPersonDto | null) => {
        if (createdContact) {
          this.contactPersons.push(createdContact);
          this.formGroup
            .get('contactPersons')
            .patchValue([...this.formGroup.get('contactPersons').value, createdContact.id]);
        }
      });
  }
}
