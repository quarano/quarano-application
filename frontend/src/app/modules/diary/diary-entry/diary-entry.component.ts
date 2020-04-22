import { ContactPersonDto } from '@models/contact-person';
import { ContactPersonDialogComponent } from './../../contact/contact-person-dialog/contact-person-dialog.component';
import { DeactivatableComponent } from '@guards/prevent-unsaved-changes.guard';
import { SubSink } from 'subsink';
import { DiaryEntryModifyDto } from '@models/diary-entry';
import { SnackbarService } from '@services/snackbar.service';
import { ApiService } from '@services/api.service';
import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { DiaryEntryDto } from '@models/diary-entry';
import { ActivatedRoute, Router } from '@angular/router';
import { SymptomDto } from '@models/symptom';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { Observable } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-diary-entry',
  templateUrl: './diary-entry.component.html',
  styleUrls: ['./diary-entry.component.scss']
})
export class DiaryEntryComponent implements OnInit, OnDestroy, DeactivatableComponent {
  formGroup: FormGroup;
  diaryEntry: DiaryEntryDto;
  nonCharacteristicSymptoms: SymptomDto[] = [];
  characteristicSymptoms: SymptomDto[] = [];
  contactPersons: ContactPersonDto[] = [];
  today = new Date();
  private subs = new SubSink();
  state: any;

  @HostListener('window:beforeunload')
  canDeactivate(): Observable<boolean> | boolean {
    return this.formGroup.pristine;
  }

  get isNew(): boolean {
    return this.diaryEntry?.id == null;
  }

  get isReadonly(): boolean {
    if (!this.isNew && this.diaryEntry._links.edit) { return true; } return false;
  }

  constructor(
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private apiService: ApiService,
    private snackbarService: SnackbarService,
    private router: Router,
    private dialog: MatDialog,
    private activatedRoute: ActivatedRoute) { }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  ngOnInit() {
    this.subs.add(this.activatedRoute.paramMap
      .subscribe(_ => this.state = window.history.state));
    this.subs.add(this.route.data.subscribe(data => {
      this.diaryEntry = data.diaryEntry;
      this.setSymptoms(data.symptoms);
      this.contactPersons = data.contactPersons;
    }));
    this.buildForm();
  }

  setSymptoms(symptoms: SymptomDto[]) {
    symptoms.forEach(symptom => {
      if (symptom.characteristic) {
        this.characteristicSymptoms.push(symptom);
      } else {
        this.nonCharacteristicSymptoms.push(symptom);
      }
    });
  }

  get nonCharacteristicSymptomIds() {
    return this.diaryEntry.nonCharacteristicSymptoms.map(s => s.id);
  }

  buildForm() {
    const characteristicSymptomIds = this.diaryEntry.characteristicSymptoms.map(s => s.id);
    const contactPersonIds = this.diaryEntry.contacts.map(c => c.id);
    this.formGroup = this.formBuilder.group(
      {
        bodyTemperature: new FormControl(
          { value: this.diaryEntry.bodyTemperature || 35, disabled: this.isReadonly },
          [Validators.required, Validators.min(35.1), Validators.max(44.0)]),
        characteristicSymptoms: new FormControl({ value: characteristicSymptomIds, disabled: this.isReadonly }),
        nonCharacteristicSymptoms: new FormControl({ value: this.nonCharacteristicSymptomIds, disabled: this.isReadonly }),
        dateTime: new FormControl({ value: this.diaryEntry.date, disabled: this.isReadonly }, Validators.required),
        contactPersons: new FormControl({ value: contactPersonIds, disabled: this.isReadonly })
      }
    );
  }

  onSubmit() {
    if (this.formGroup.valid) {
      const diaryEntryModifyDto: DiaryEntryModifyDto
        = { id: null, bodyTemperature: null, symptoms: [], slot: { date: null, timeOfDay: null }, contacts: [] };
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

  getTitle(): string {
    if (this.isNew) {
      return `Tagebuch-Eintrag anlegen für den ` +
        `${new Date(this.state.date).toLocaleDateString()} ` +
        `(${this.state.slot === 'MORNING' ? 'morgens' : 'abends'})`;
    } else {
      return `Tagebuch-Eintrag bearbeiten für den ` +
        `${new Date(this.diaryEntry.slot.date).toLocaleDateString()} ` +
        `(${this.diaryEntry.slot.timeOfDay === 'MORNING' ? 'morgens' : 'abends'})`;
    }
  }

  createEntry(diaryEntry: DiaryEntryModifyDto) {
    diaryEntry.slot.date = this.state.date;
    diaryEntry.slot.timeOfDay = this.state.slot;
    this.subs.add(this.apiService
      .createDiaryEntry(diaryEntry)
      .subscribe(_ => {
        this.snackbarService.success('Tagebuch-Eintrag erfolgreich angelegt');
        this.formGroup.markAsPristine();
        this.router.navigate(['/diary']);
      }));
  }

  modifyEntry(diaryEntry: DiaryEntryModifyDto) {
    diaryEntry.slot = this.diaryEntry.slot;
    this.subs.add(this.apiService
      .modifyDiaryEntry(diaryEntry)
      .subscribe(_ => {
        this.snackbarService.success('Tagebuch-Eintrag erfolgreich aktualisiert');
        this.formGroup.markAsPristine();
        this.router.navigate(['/diary']);
      }));
  }

  onSlideToggleChanged(event: MatSlideToggleChange, symptomId: string) {
    const control = this.characteristicSymptomsControl;
    let values = control.value as string[];

    if (event.checked) {
      values.push(symptomId);
    } else {
      values = values.filter(value => value !== symptomId);
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
    if (value === 0) { return ''; }
    return value.toLocaleString();
  }

  openContactDialog() {
    const dialogRef = this.dialog.open(ContactPersonDialogComponent, {
      height: '90vh',
      maxWidth: '100vw',
      data: {
        contactPerson: { id: null, lastName: null, firstName: null, phone: null, email: null },
      }
    });

    this.subs.add(dialogRef.afterClosed().subscribe((createdContact: ContactPersonDto | null) => {
      if (createdContact) {
        this.contactPersons.push(createdContact);
      }
    }));
  }
}
