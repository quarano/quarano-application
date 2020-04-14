import { EncounterDto } from './../models/encounter';
import { EnrollmentService } from './../services/enrollment.service';
import { ClientDto } from './../models/client';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { QuestionnaireDto } from './../models/first-query';
import { ActivatedRoute } from '@angular/router';
import { SubSink } from 'subsink';
import { ContactPersonDto } from 'src/app/models/contact-person';
import { FormGroup, FormBuilder, Validators, FormControl, AbstractControl } from '@angular/forms';
import { Component, OnInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import '../utils/date-extensions';
import { MatDialog } from '@angular/material/dialog';
import { ContactPersonDialogComponent } from '../contact/contact-person-dialog/contact-person-dialog.component';
import { StepperSelectionEvent } from '@angular/cdk/stepper';
import { Moment } from 'moment';
import { VALIDATION_PATTERNS } from '../utils/validation';
import { debounceTime } from 'rxjs/operators';
import { MatHorizontalStepper } from '@angular/material/stepper';
import { MatCheckboxChange } from '@angular/material/checkbox';

@Component({
  selector: 'app-basic-data',
  templateUrl: './basic-data.component.html',
  styleUrls: ['./basic-data.component.scss']
})
export class BasicDataComponent implements OnInit, OnDestroy {
  subs = new SubSink();
  today = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());
  @ViewChild('stepper') stepper: MatHorizontalStepper;

  // ########## STEP I ##########
  firstFormGroup: FormGroup;
  client: ClientDto;

  // ########## STEP II ##########
  secondFormGroup: FormGroup;
  firstQuery: QuestionnaireDto;

  // ########## STEP III ##########
  thirdFormGroup: FormGroup;
  datesForRetrospectiveContacts: Date[] = [];
  contactPersons: ContactPersonDto[] = [];
  encounters: EncounterDto[] = [];
  noRetrospectiveContactsConfirmed = false;

  constructor(
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    private route: ActivatedRoute,
    private snackbarService: SnackbarService,
    private enrollmentService: EnrollmentService) { }

  ngOnInit() {
    this.subs.add(this.route.data.subscribe(data => {
      this.contactPersons = data.contactPersons;
      this.firstQuery = data.firstQuery;
      this.client = data.clientData;
      this.encounters = data.encounters;
      this.buildForms();
    }));

    this.enrollmentService.getEnrollmentStatus()
      .subscribe(status => {
        if (status.completedPersonalData) {
          this.stepper.next();
        }
        if (status.completedQuestionnaire) {
          this.stepper.next();
        }
      });
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  buildForms() {
    this.buildFirstForm();
    this.buildSecondForm();
    this.buildThirdForm();
  }

  onTabChanged(event: StepperSelectionEvent) {
    if (event.selectedIndex === 2) {
      this.buildThirdForm();
    }
  }

  // ########## STEP I ##########

  buildFirstForm() {
    this.firstFormGroup = this.formBuilder.group({
      firstName: new FormControl(this.client.firstName, [Validators.required]),
      lastName: new FormControl(this.client.lastName, [Validators.required]),
      email: new FormControl(this.client.email, [Validators.required, Validators.email]),
      phone: new FormControl(this.client.phone,
        [Validators.minLength(5), Validators.maxLength(17), Validators.pattern(VALIDATION_PATTERNS.phoneNumber)]),
      mobilePhone: new FormControl(this.client.mobilePhone,
        [Validators.minLength(5), Validators.maxLength(17), Validators.pattern(VALIDATION_PATTERNS.phoneNumber)]),
      street: new FormControl(this.client.street, [Validators.required]),
      houseNumber: new FormControl(this.client.houseNumber, [Validators.maxLength(6)]),
      zipCode: new FormControl(this.client.zipCode,
        [Validators.required, Validators.minLength(5), Validators.maxLength(5), Validators.pattern(VALIDATION_PATTERNS.integerUnsigned)]),
      city: new FormControl(this.client.city, [Validators.required]),
      dateOfBirth: new FormControl(this.client.dateOfBirth, [Validators.required])
    });

    this.firstFormGroup.statusChanges
      .pipe(debounceTime(1000))
      .subscribe((status) => {
        if (status === 'VALID' && !this.firstFormGroup.pristine) {
          const value = this.firstFormGroup.value;
          value.dateOfBirth = this.dateOfBirth;
          this.enrollmentService.updatePersonalDetails(value)
            .subscribe(_ => {
              this.client = value;
              this.snackbarService.success('Persönliche Daten erfolgreich gespeichert');
            });
        }
      });
  }

  get dateOfBirth() {
    const dateValue = this.firstFormGroup?.controls.dateOfBirth.value;
    if (dateValue?.toDate instanceof Function) {
      return (dateValue as Moment).toDate();
    }
    if (dateValue) {
      return dateValue as Date;
    }
    return null;
  }

  // ########## STEP II ##########

  get dayOfFirstSymptoms() {
    const dateValue = this.secondFormGroup?.controls.dayOfFirstSymptoms.value;
    if (dateValue?.toDate instanceof Function) {
      return (dateValue as Moment).toDate();
    }
    if (dateValue) {
      return dateValue as Date;
    }
    return null;
  }

  buildSecondForm() {
    this.secondFormGroup = new FormGroup({
      min15MinutesContactWithC19Pat: new FormControl(this.firstQuery.min15MinutesContactWithC19Pat, [Validators.required]),
      nursingActionOnC19Pat: new FormControl(this.firstQuery.nursingActionOnC19Pat, [Validators.required]),
      directContactWithLiquidsOfC19pat: new FormControl(this.firstQuery.directContactWithLiquidsOfC19pat, [Validators.required]),
      flightPassengerCloseRowC19Pat: new FormControl(this.firstQuery.flightPassengerCloseRowC19Pat, [Validators.required]),
      flightCrewMemberWithC19Pat: new FormControl(this.firstQuery.flightCrewMemberWithC19Pat, [Validators.required]),
      belongToMedicalStaff: new FormControl(this.firstQuery.belongToMedicalStaff, [Validators.required]),
      belongToNursingStaff: new FormControl(this.firstQuery.belongToNursingStaff, [Validators.required]),
      belongToLaboratoryStaff: new FormControl(this.firstQuery.belongToLaboratoryStaff, [Validators.required]),
      familyMember: new FormControl(this.firstQuery.familyMember, [Validators.required]),
      dayOfFirstSymptoms: new FormControl(this.firstQuery.dayOfFirstSymptoms),
      otherContactType: new FormControl(this.firstQuery.otherContactType),
      hasSymptoms: new FormControl(this.firstQuery.hasSymptoms, [Validators.required])
    });

    this.secondFormGroup.controls.hasSymptoms.valueChanges.subscribe((value: boolean) => {
      const control = this.secondFormGroup.controls.dayOfFirstSymptoms;

      if (!value) {
        control.clearValidators();
        this.secondFormGroup.updateValueAndValidity();
        control.setValue(null);
      } else {
        control.setValidators(Validators.required);
        this.secondFormGroup.updateValueAndValidity();
      }
    });

    this.secondFormGroup.statusChanges
      .pipe(debounceTime(1000)).subscribe((status) => {
        if (status === 'VALID' && !this.secondFormGroup.pristine) {
          const value = this.secondFormGroup.value;
          value.dayOfFirstSymptoms = this.dayOfFirstSymptoms;
          this.enrollmentService.updateFirstQuery(value)
            .subscribe(_ => {
              this.firstQuery = value;
              this.snackbarService.success('Fragebogen erfolgreich gespeichert');
            });
        }
      });
  }

  firstSymptomsValidator(g: FormGroup) {
    if (g.controls.hasSymptoms.value === true) {
      return g.controls.dayOfFirstSymptoms.value ? null : { required: true };
    }
    return null;
  }

  // ########## STEP III ##########

  buildThirdForm() {
    this.thirdFormGroup = this.formBuilder.group({
      noRetrospectiveContactsConfirmed: new FormControl(false)
    });
    let day = new Date(this.today);
    this.datesForRetrospectiveContacts = [];
    const firstSymptomsDay = this.firstQuery.dayOfFirstSymptoms || new Date(this.today);
    const firstDay = firstSymptomsDay.addDays(-2);
    while (day.getDateWithoutTime() >= firstDay.getDateWithoutTime()) {
      this.datesForRetrospectiveContacts.push(day);
      this.thirdFormGroup.addControl(day.toLocaleDateString(), new FormControl(this.encounters
        .filter(e => e.date.getDateWithoutTime() === day.getDateWithoutTime())));
      day = day.addDays(-1);
    }
  }

  openContactDialog() {
    const dialogRef = this.dialog.open(ContactPersonDialogComponent, {
      height: '90vh',
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

  onContactAdded(date: Date, id: string) {
    this.enrollmentService.createEncounter({ date, contact: id })
      .subscribe(_ => {
        this.snackbarService.success('Kontakt erfolgreich gespeichert');
      });
  }

  onContactRemoved(date: Date, id: string) {
    // ToDo: call api delete
    this.snackbarService.success('Kontakt erfolgreich entfernt');
  }

  hasRetrospectiveContacts(): boolean {
    let result = false;
    Object.keys(this.thirdFormGroup.controls).forEach(key => {
      if (key !== 'noRetrospectiveContactsConfirmed') {
        if (this.thirdFormGroup.controls[key].value.length > 0) {
          result = true;
        }
      }
    });
    return result;
  }

  hasNoRetrospectiveContactsConfirmed(): boolean {
    return this.thirdFormGroup.controls.noRetrospectiveContactsConfirmed.value;
  }

  onCheckboxChanged(event: MatCheckboxChange) {
    if (event.checked) {
      this.enrollmentService.completeEnrollment(true)
        .subscribe(_ => this.snackbarService.success('Die Registrierung wurde als abgeschlossen markiert'));
    } else {
      this.enrollmentService.reopenEnrollment()
        .subscribe(_ => this.snackbarService.success('Die Registrierung wurde wieder zum Bearbeiten freigeschaltet'));
    }
  }
}
