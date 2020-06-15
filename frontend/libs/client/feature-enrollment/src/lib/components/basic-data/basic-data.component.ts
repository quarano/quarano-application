import { ClientService, EncounterEntry } from '@qro/client/domain';
import { BadRequestService } from '@qro/shared/ui-error';
import { ActivatedRoute, Router } from '@angular/router';
import { SubSink } from 'subsink';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AfterViewChecked, ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { StepperSelectionEvent } from '@angular/cdk/stepper';
import { Moment } from 'moment';
import { MatHorizontalStepper } from '@angular/material/stepper';
import { BehaviorSubject } from 'rxjs';
import { EnrollmentStatusDto, EnrollmentService } from '@qro/client/domain';
import { SymptomDto } from '@qro/shared/util-symptom';
import { ContactPersonDto } from '@qro/client/domain';
import { SnackbarService } from '@qro/shared/util-snackbar';
import { TrimmedPatternValidator, VALIDATION_PATTERNS, PhoneOrMobilePhoneValidator } from '@qro/shared/util-forms';
import { ConfirmationDialogComponent } from '@qro/shared/ui-confirmation-dialog';
import { DateFunctions } from '@qro/shared/util-date';
import { ContactPersonDialogComponent } from '@qro/client/ui-contact-person-detail';
import { ClientDto } from '@qro/auth/api';
import { QuestionnaireDto } from '@qro/shared/util-data-access';
import {
  ConfirmDialogData,
  QroDialogService,
} from '../../../../../../../apps/quarano-frontend/src/app/services/qro-dialog.service';

@Component({
  selector: 'qro-basic-data',
  templateUrl: './basic-data.component.html',
  styleUrls: ['./basic-data.component.scss'],
})
export class BasicDataComponent implements OnInit, OnDestroy, AfterViewChecked {
  subs = new SubSink();
  today = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());

  enrollmentStatus$$ = new BehaviorSubject<EnrollmentStatusDto>({
    completedContactRetro: null,
    completedPersonalData: null,
    completedQuestionnaire: null,
    complete: null,
  });

  @ViewChild('stepper')
  stepper: MatHorizontalStepper;

  // ########## STEP I ##########
  firstFormGroup: FormGroup;
  client: ClientDto;
  firstFormLoading = false;

  // ########## STEP II ##########
  secondFormGroup: FormGroup;
  firstQuery: QuestionnaireDto;
  symptoms: SymptomDto[];
  secondFormLoading = false;

  // ########## STEP III ##########
  thirdFormGroup: FormGroup;
  datesForRetrospectiveContacts: Date[] = [];
  contactPersons: ContactPersonDto[] = [];
  encounters: EncounterEntry[] = [];
  noRetrospectiveContactsConfirmed = false;
  thirdFormLoading = false;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private snackbarService: SnackbarService,
    private enrollmentService: EnrollmentService,
    private router: Router,
    private changeDetect: ChangeDetectorRef,
    private badRequestService: BadRequestService,
    private clientService: ClientService,
    private dialogService: QroDialogService
  ) {}

  ngOnInit() {
    this.subs.add(
      this.route.data.subscribe((data) => {
        this.contactPersons = data.contactPersons;
        this.firstQuery = data.firstQuery;
        this.client = data.clientData;
        this.encounters = data.encounters;
        this.symptoms = data.symptoms.filter((symptom) => symptom.characteristic);

        this.buildForms();
      })
    );

    this.subs.add(
      this.enrollmentService.loadEnrollmentStatus().subscribe((status) => {
        this.enrollmentStatus$$.next(status);
        if (status.completedPersonalData) {
          this.stepper.next();
        }
        if (status.completedQuestionnaire) {
          this.stepper.next();
        }
      })
    );
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

  onTabChanged(event: StepperSelectionEvent, status: EnrollmentStatusDto) {
    if (event.previouslySelectedIndex === 0 && event.selectedIndex === 1 && status.completedPersonalData === false) {
      this.checkAndSendFirstForm();
    }

    if (event.previouslySelectedIndex === 1 && event.selectedIndex === 2 && !status.completedContactRetro === false) {
      this.checkAndSendQuestionaire();
    }

    if (event.selectedIndex === 2) {
      this.buildThirdForm();
    }
  }

  // ########## STEP I ##########

  buildFirstForm() {
    this.firstFormGroup = this.formBuilder.group(
      {
        firstName: new FormControl(this.client.firstName, [
          Validators.required,
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.name),
        ]),
        lastName: new FormControl(this.client.lastName, [
          Validators.required,
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.name),
        ]),
        email: new FormControl(this.client.email, [
          Validators.required,
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.email),
        ]),
        phone: new FormControl(this.client.phone, [
          Validators.minLength(5),
          Validators.maxLength(17),
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.phoneNumber),
        ]),
        mobilePhone: new FormControl(this.client.mobilePhone, [
          Validators.minLength(5),
          Validators.maxLength(17),
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.phoneNumber),
        ]),
        street: new FormControl(this.client.street, [
          Validators.required,
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.street),
        ]),
        houseNumber: new FormControl(this.client.houseNumber, [
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.houseNumber),
        ]),
        zipCode: new FormControl(this.client.zipCode, [
          Validators.required,
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.zip),
        ]),
        city: new FormControl(this.client.city, [
          Validators.required,
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.city),
        ]),
        dateOfBirth: new FormControl(this.client.dateOfBirth, [Validators.required]),
      },
      { validators: [PhoneOrMobilePhoneValidator] }
    );
  }

  checkAndSendFirstForm() {
    if (this.firstFormGroup.valid) {
      this.firstFormLoading = true;
      const value = this.firstFormGroup.value;
      value.dateOfBirth = this.dateOfBirth;
      this.clientService
        .updatePersonalDetails(value)
        .subscribe(
          (result) => {
            this.enrollmentStatus$$.next(result);
            if (result.completedPersonalData) {
              this.client = value;
              this.snackbarService.success('Persönliche Daten erfolgreich gespeichert');
              this.stepper.next();
            }
            this.firstFormLoading = false;
          },
          (error) => {
            this.badRequestService.handleBadRequestError(error, this.firstFormGroup);
          }
        )
        .add(() => (this.firstFormLoading = false));
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
      familyDoctor: new FormControl(this.firstQuery.familyDoctor, [
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.textual),
      ]),
      guessedOriginOfInfection: new FormControl(this.firstQuery.guessedOriginOfInfection, [
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.textual),
      ]),
      hasPreExistingConditions: new FormControl(this.firstQuery.hasPreExistingConditions, [Validators.required]),
      hasPreExistingConditionsDescription: new FormControl(this.firstQuery.hasPreExistingConditionsDescription),
      belongToMedicalStaff: new FormControl(this.firstQuery.belongToMedicalStaff, [Validators.required]),
      belongToMedicalStaffDescription: new FormControl(this.firstQuery.belongToMedicalStaffDescription),
      hasContactToVulnerablePeople: new FormControl(this.firstQuery.hasContactToVulnerablePeople, [
        Validators.required,
      ]),
      hasContactToVulnerablePeopleDescription: new FormControl(this.firstQuery.hasContactToVulnerablePeopleDescription),
    });
  }

  checkAndSendQuestionaire() {
    if (this.secondFormGroup.valid) {
      this.secondFormLoading = true;
      const questionaireData: QuestionnaireDto = { ...this.secondFormGroup.value };

      if (this.secondFormGroup.get('symptoms').value) {
        questionaireData.symptoms = this.secondFormGroup.get('symptoms').value.map((data) => data.id);
      }

      this.subs.add(
        this.enrollmentService
          .updateQuestionnaire(this.secondFormGroup.value)
          .subscribe(
            () => {
              this.firstQuery = this.secondFormGroup.value;
              this.snackbarService.success('Fragebogen erfolgreich gespeichert');

              this.stepper.next();
            },
            (error) => {
              this.badRequestService.handleBadRequestError(error, this.secondFormGroup);
            }
          )
          .add(() => (this.secondFormLoading = false))
      );
    }
  }

  // ########## STEP III ##########

  buildThirdForm() {
    this.thirdFormGroup = new FormGroup({});
    let day = new Date(this.today);
    this.datesForRetrospectiveContacts = [];

    const firstSymptomsDay = new Date(Date.parse(this.firstQuery.dayOfFirstSymptoms) || this.today);
    const firstDay = DateFunctions.addDays(firstSymptomsDay, -2);
    while (DateFunctions.getDateWithoutTime(day) >= DateFunctions.getDateWithoutTime(firstDay)) {
      this.datesForRetrospectiveContacts.push(day);
      this.thirdFormGroup.addControl(
        day.toLocaleDateString('de'),
        new FormControl(
          this.encounters.filter((e) => e.date === DateFunctions.getDateWithoutTime(day)).map((e) => e.contactPersonId)
        )
      );
      day = DateFunctions.addDays(day, -1);
    }
  }

  openContactDialog(dateString: string) {
    this.subs.add(
      this.dialogService
        .openContactPersonDialog()
        .afterClosed()
        .subscribe((createdContact: ContactPersonDto | null) => {
          if (createdContact) {
            this.contactPersons.push(createdContact);
            this.thirdFormGroup.controls[dateString].patchValue([
              ...this.thirdFormGroup.controls[dateString].value,
              createdContact.id,
            ]);
          }
        })
    );
  }

  onContactAdded(date: Date, id: string) {
    this.enrollmentService
      .createEncounter({ date: DateFunctions.getDateWithoutTime(date), contact: id })
      .subscribe((encounter) => {
        this.encounters.push(encounter);
        this.snackbarService.success('Kontakt erfolgreich gespeichert');
      });
  }

  onContactRemoved(date: Date, id: string) {
    const encounterToRemove = this.encounters.find(
      (e) => e.contactPersonId === id && e.date === DateFunctions.getDateWithoutTime(date)
    );
    this.enrollmentService.deleteEncounter(encounterToRemove.encounter).subscribe((_) => {
      this.encounters = this.encounters.filter((e) => e !== encounterToRemove);
      this.snackbarService.success('Kontakt erfolgreich entfernt');
    });
  }

  hasRetrospectiveContacts(): boolean {
    let result = false;
    Object.keys(this.thirdFormGroup.controls).forEach((key) => {
      if (this.thirdFormGroup.controls[key].value.length > 0) {
        result = true;
      }
    });
    return result;
  }

  onComplete() {
    this.thirdFormLoading = true;
    if (!this.hasRetrospectiveContacts()) {
      const data: ConfirmDialogData = {
        title: 'Keine relevanten Kontakte?',
        text:
          'Sie haben noch keinen retrospektiven Kontakt erfasst. ' +
          'Bitte bestätigen Sie, dass Sie im genannten Zeitraum keinerlei relevanten Kontakte zu anderen Personen hatten',
      };

      this.dialogService
        .openConfirmDialog({ data: data })
        .afterClosed()
        .subscribe((result) => {
          if (result) {
            this.completeEnrollment(true);
          }
        })
        .add(() => (this.thirdFormLoading = false));
    } else {
      this.completeEnrollment(false);
    }
  }

  private completeEnrollment(withoutEncounters: boolean) {
    this.enrollmentService
      .completeEnrollment(withoutEncounters)
      .subscribe((result) => {
        this.enrollmentStatus$$.next(result);
        if (result.completedContactRetro) {
          this.snackbarService.success('Die Registrierung wurde abgeschlossen');
          this.router.navigate(['/client/diary/diary-list']);
        }
      })
      .add(() => (this.thirdFormLoading = false));
  }
}
