import {EnrollmentStatusDto} from '@models/enrollment-status';
import {EncounterEntry} from '@models/encounter';
import {EnrollmentService} from '@services/enrollment.service';
import {ClientDto} from '@models/client';
import {SnackbarService} from '@services/snackbar.service';
import {QuestionnaireDto} from '@models/first-query';
import {ActivatedRoute, Router} from '@angular/router';
import {SubSink} from 'subsink';
import {ContactPersonDto} from '@models/contact-person';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {AfterViewChecked, ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import '@utils/date-extensions';
import {MatDialog} from '@angular/material/dialog';
import {ContactPersonDialogComponent} from '../app-forms/contact-person-dialog/contact-person-dialog.component';
import {StepperSelectionEvent} from '@angular/cdk/stepper';
import {Moment} from 'moment';
import {VALIDATION_PATTERNS} from '@utils/validation';
import {MatHorizontalStepper} from '@angular/material/stepper';
import {ConfirmationDialogComponent} from '@ui/confirmation-dialog/confirmation-dialog.component';
import {BehaviorSubject} from 'rxjs';
import {SymptomDto} from '@models/symptom';

@Component({
  selector: 'app-basic-data',
  templateUrl: './basic-data.component.html',
  styleUrls: ['./basic-data.component.scss']
})
export class BasicDataComponent implements OnInit, OnDestroy, AfterViewChecked {
  subs = new SubSink();
  today = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());
  enrollmentStatus$$ = new BehaviorSubject<EnrollmentStatusDto>(null);
  @ViewChild('stepper') stepper: MatHorizontalStepper;

  // ########## STEP I ##########
  firstFormGroup: FormGroup;
  client: ClientDto;

  // ########## STEP II ##########
  secondFormGroup: FormGroup;
  firstQuery: QuestionnaireDto;
  symptoms: SymptomDto[];

  // ########## STEP III ##########
  thirdFormGroup: FormGroup;
  datesForRetrospectiveContacts: Date[] = [];
  contactPersons: ContactPersonDto[] = [];
  encounters: EncounterEntry[] = [];
  noRetrospectiveContactsConfirmed = false;

  constructor(
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    private route: ActivatedRoute,
    private snackbarService: SnackbarService,
    private enrollmentService: EnrollmentService,
    private router: Router,
    private changeDetect: ChangeDetectorRef) {
  }

  ngOnInit() {
    this.subs.add(this.route.data.subscribe(data => {
      this.contactPersons = data.contactPersons;
      this.firstQuery = data.firstQuery;
      this.client = data.clientData;
      this.encounters = data.encounters;
      this.symptoms = data.symptoms.filter((symptom) => symptom.characteristic);

      this.buildForms();
    }));

    this.subs.add(this.enrollmentService.getEnrollmentStatus()
      .subscribe(status => {
        this.enrollmentStatus$$.next(status);
        if (status.completedPersonalData) {
          this.stepper.next();
        }
        if (status.completedQuestionnaire) {
          this.stepper.next();
        }
      }));
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  ngAfterViewChecked(): void {
    this.changeDetect.detectChanges();
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
      firstName: new FormControl(this.client.firstName, [Validators.required, Validators.pattern(VALIDATION_PATTERNS.name)]),
      lastName: new FormControl(this.client.lastName, [Validators.required, Validators.pattern(VALIDATION_PATTERNS.name)]),
      email: new FormControl(this.client.email, [Validators.required, Validators.pattern(VALIDATION_PATTERNS.email)]),
      phone: new FormControl(this.client.phone,
        [Validators.minLength(5), Validators.maxLength(17), Validators.pattern(VALIDATION_PATTERNS.phoneNumber)]),
      mobilePhone: new FormControl(this.client.mobilePhone,
        [Validators.minLength(5), Validators.maxLength(17), Validators.pattern(VALIDATION_PATTERNS.phoneNumber)]),
      street: new FormControl(this.client.street, [Validators.required, Validators.pattern(VALIDATION_PATTERNS.street)]),
      houseNumber: new FormControl(this.client.houseNumber, [Validators.pattern(VALIDATION_PATTERNS.houseNumber)]),
      zipCode: new FormControl(this.client.zipCode,
        [Validators.required, Validators.minLength(5), Validators.maxLength(5), Validators.pattern(VALIDATION_PATTERNS.zip)]),
      city: new FormControl(this.client.city, [Validators.required]),
      dateOfBirth: new FormControl(this.client.dateOfBirth, [Validators.required])
    });
  }

  checkAndSendFirstForm() {
    if (this.firstFormGroup.valid) {
      const value = this.firstFormGroup.value;
      value.dateOfBirth = this.dateOfBirth;
      this.enrollmentService.updatePersonalDetails(value)
        .subscribe(result => {
          this.enrollmentStatus$$.next(result);
          if (result.completedPersonalData) {
            this.client = value;
            this.snackbarService.success('Persönliche Daten erfolgreich gespeichert');
            this.stepper.next();
          }
        });
    }
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

    const symptoms = this.firstQuery?.symptoms || [];

    this.secondFormGroup = new FormGroup({
      hasSymptoms: new FormControl(this.firstQuery.hasSymptoms, [Validators.required]),
      dayOfFirstSymptoms: new FormControl(this.firstQuery.dayOfFirstSymptoms),
      symptoms: new FormControl(symptoms),
      familyDoctor: new FormControl(this.firstQuery.familyDoctor, [Validators.required]),
      guessedOriginOfInfection: new FormControl(this.firstQuery.guessedOriginOfInfection, [Validators.required]),
      hasPreExistingConditions: new FormControl(this.firstQuery.hasPreExistingConditions, [Validators.required]),
      hasPreExistingConditionsDescription: new FormControl(this.firstQuery.hasPreExistingConditionsDescription),
      belongToMedicalStaff: new FormControl(this.firstQuery.belongToMedicalStaff, [Validators.required]),
      belongToMedicalStaffDescription: new FormControl(this.firstQuery.belongToMedicalStaffDescription),
      hasContactToVulnerablePeople: new FormControl(this.firstQuery.hasContactToVulnerablePeople, [Validators.required]),
      hasContactToVulnerablePeopleDescription: new FormControl(this.firstQuery.hasContactToVulnerablePeopleDescription)
    });
  }

  checkAndSendQuestionaire() {
    if (this.secondFormGroup.valid) {
      const questionaireData: QuestionnaireDto = {...this.secondFormGroup.value};

      if (this.secondFormGroup.get('symptoms').value) {
        questionaireData.symptoms = this.secondFormGroup.get('symptoms').value.map((data) => data.id);
      }

      this.subs.add(this.enrollmentService.updateQuestionnaire(this.secondFormGroup.value)
          .subscribe(() => {
              this.firstQuery = this.secondFormGroup.value;
              this.snackbarService.success('Fragebogen erfolgreich gespeichert');


          this.stepper.next();
        }));
    }
  }

  // ########## STEP III ##########

  buildThirdForm() {
    this.thirdFormGroup = new FormGroup({});
    let day = new Date(this.today);
    this.datesForRetrospectiveContacts = [];

    const firstSymptomsDay = new Date(Date.parse(this.firstQuery.dayOfFirstSymptoms) || this.today);
    const firstDay = firstSymptomsDay.addDays(-2);
    while (day.getDateWithoutTime() >= firstDay.getDateWithoutTime()) {
      this.datesForRetrospectiveContacts.push(day);
      this.thirdFormGroup.addControl(day.toLocaleDateString('de'), new FormControl(this.encounters
        .filter(e => e.date === day.getDateWithoutTime())
        .map(e => e.contactPersonId)));
      day = day.addDays(-1);
    }
  }

  openContactDialog(dateString: string) {
    const dialogRef = this.dialog.open(ContactPersonDialogComponent, {
      height: '90vh',
      data: {
        contactPerson: {id: null, lastName: null, firstName: null, phone: null, email: null},
      }
    });

    this.subs.add(dialogRef.afterClosed().subscribe((createdContact: ContactPersonDto | null) => {
      if (createdContact) {
        this.contactPersons.push(createdContact);
        this.thirdFormGroup.controls[dateString].patchValue([...this.thirdFormGroup.controls[dateString].value, createdContact.id]);
      }
    }));
  }

  onContactAdded(date: Date, id: string) {
    this.enrollmentService.createEncounter({date: date.getDateWithoutTime(), contact: id})
      .subscribe(encounter => {
        this.encounters.push(encounter);
        this.snackbarService.success('Kontakt erfolgreich gespeichert');
      });
  }

  onContactRemoved(date: Date, id: string) {
    const encounterToRemove = this.encounters.find(e =>
      e.contactPersonId === id
      && e.date === date.getDateWithoutTime());
    this.enrollmentService.deleteEncounter(encounterToRemove.encounter)
      .subscribe(_ => {
        this.encounters = this.encounters.filter(e => e !== encounterToRemove);
        this.snackbarService.success('Kontakt erfolgreich entfernt');
      });
  }

  hasRetrospectiveContacts(): boolean {
    let result = false;
    Object.keys(this.thirdFormGroup.controls).forEach(key => {
      if (this.thirdFormGroup.controls[key].value.length > 0) {
        result = true;
      }
    });
    return result;
  }

  onComplete() {
    if (!this.hasRetrospectiveContacts()) {
      const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
        data: {
          title: 'Keine relevanten Kontakte?',
          text:
            'Sie haben noch keinen retrospektiven Kontakt erfasst. ' +
            'Bitte bestätigen Sie, dass Sie im genannten Zeitraum keinerlei relevanten Kontakte zu anderen Personen hatten'
        }
      });

      dialogRef.afterClosed()
        .subscribe(result => {
          if (result) {
            this.completeEnrollment(true);
          }
        });
    } else {
      this.completeEnrollment(false);
    }
  }

  private completeEnrollment(withoutEncounters: boolean) {
    this.enrollmentService.completeEnrollment(withoutEncounters)
      .subscribe(result => {
        this.enrollmentStatus$$.next(result);
        if (result.completedContactRetro) {
          this.snackbarService.success('Die Registrierung wurde abgeschlossen');
          this.router.navigate(['/diary']);
        }
      });
  }
}
