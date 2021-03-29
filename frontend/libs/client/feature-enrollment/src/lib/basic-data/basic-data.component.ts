import { EncounterCreateDto } from './../../../../domain/src/lib/model/encounter';
import { EncounterFormComponent } from './../encounter-form/encounter-form.component';
import { TranslateService } from '@ngx-translate/core';
import { SymptomSelectors } from '@qro/shared/util-symptom';
import { select, Store } from '@ngrx/store';
import { EncounterEntry, ClientStore, ZipCodeErrorDto, HealthDepartmentDto, LocationDto } from '@qro/client/domain';
import { BadRequestService } from '@qro/shared/ui-error';
import { ActivatedRoute, Router } from '@angular/router';
import { SubSink } from 'subsink';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import {
  AfterViewChecked,
  ChangeDetectorRef,
  Component,
  OnDestroy,
  OnInit,
  ViewChild,
  AfterViewInit,
  ViewContainerRef,
  ComponentFactory,
  ComponentFactoryResolver,
  ComponentRef,
  ViewChildren,
  QueryList,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { StepperSelectionEvent } from '@angular/cdk/stepper';
import { Moment } from 'moment';
import { MatHorizontalStepper } from '@angular/material/stepper';
import { EnrollmentStatusDto, EnrollmentService } from '@qro/client/domain';
import { SymptomDto } from '@qro/shared/util-symptom';
import { ContactPersonDto } from '@qro/client/domain';
import { TranslatedSnackbarService } from '@qro/shared/util-snackbar';
import { TrimmedPatternValidator, VALIDATION_PATTERNS, PhoneOrMobilePhoneValidator } from '@qro/shared/util-forms';
import { ConfirmationDialogComponent, TranslatedConfirmationDialogComponent } from '@qro/shared/ui-confirmation-dialog';
import { DateFunctions } from '@qro/shared/util-date';
import { ClientDto, UserService } from '@qro/auth/api';
import { QuestionnaireDto } from '@qro/shared/util-data-access';
import { tap, finalize, take, switchMap, map, mergeMap, filter, first } from 'rxjs/operators';
import { iif, noop, Observable, of } from 'rxjs';

@Component({
  selector: 'qro-basic-data',
  templateUrl: './basic-data.component.html',
  styleUrls: ['./basic-data.component.scss'],
})
export class BasicDataComponent implements OnInit, OnDestroy, AfterViewChecked, AfterViewInit {
  subs = new SubSink();
  today = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());

  @ViewChild('stepper')
  stepper: MatHorizontalStepper;
  @ViewChildren('encounter_container', { read: ViewContainerRef }) containerQueryList: QueryList<ViewContainerRef>;

  // ########## STEP I ##########
  firstFormGroup: FormGroup;
  client: ClientDto;
  firstFormLoading = false;

  // ########## STEP II ##########
  secondFormGroup: FormGroup;
  firstQuery: QuestionnaireDto;
  symptoms$: Observable<SymptomDto[]>;
  secondFormLoading = false;

  // ########## STEP III ##########
  thirdFormLoading: boolean;
  datesForRetrospectiveContacts: Date[] = [];
  contactPersons: ContactPersonDto[] = [];
  locations: LocationDto[] = [];
  encounters: EncounterEntry[] = [];
  noRetrospectiveContactsConfirmed = false;
  showThirdStep = true;
  encounterForms = new Map<Date, ComponentRef<EncounterFormComponent>[]>();

  constructor(
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    private route: ActivatedRoute,
    private snackbarService: TranslatedSnackbarService,
    private enrollmentService: EnrollmentService,
    private router: Router,
    private changeDetect: ChangeDetectorRef,
    private badRequestService: BadRequestService,
    public clientStore: ClientStore,
    private store: Store,
    private translate: TranslateService,
    private userService: UserService,
    private componentFactoryResolver: ComponentFactoryResolver
  ) {}

  ngOnInit() {
    this.subs.add(
      this.route.data.subscribe((data) => {
        this.contactPersons = data.contactPersons;
        this.locations = data.locations;
        this.firstQuery = data.firstQuery;
        this.client = data.clientData;
        this.encounters = data.encounters;
        this.buildForms();
      })
    );

    this.symptoms$ = this.store.pipe(select(SymptomSelectors.characteristicSymptoms));
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  ngAfterViewInit(): void {
    this.subs.add(
      this.clientStore
        .getEnrollmentStatus()
        .pipe(take(1))
        .subscribe((status) => {
          this.showThirdStep = status.steps.includes('encounters');
          if (status.completedPersonalData) {
            this.stepper?.next();
          }
          if (status.completedQuestionnaire) {
            this.stepper?.next();
          }
        })
    );
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

  checkAndSendFirstForm(confirmedZipCode = false) {
    if (this.firstFormGroup.valid) {
      this.firstFormLoading = true;
      const value = this.firstFormGroup.value as ClientDto;
      value.dateOfBirth = this.dateOfBirth;
      this.subs.add(
        this.enrollmentService
          .updatePersonalDetails(value, confirmedZipCode)
          .pipe(
            mergeMap((result) =>
              iif(
                () => confirmedZipCode,
                this.logoutAndNavigate(result as HealthDepartmentDto),
                this.clientStore.enrollmentStatus$.pipe(
                  filter((enrollment) => enrollment.completedPersonalData),
                  tap((enrollment) => {
                    this.client = value;
                    this.stepper.next();
                    this.firstFormLoading = false;
                  }),
                  first()
                )
              )
            )
          )
          .subscribe(
            () => {
              this.snackbarService.success('BASIC_DATA.PERSÖNLICHE_DATEN_GESPEICHERT');
            },
            (error) => {
              if (error.hasOwnProperty('unprocessableEntityErrors')) {
                this.handleUnprocessableEntityError(error.unprocessableEntityErrors);
              } else {
                this.badRequestService.handleBadRequestError(error, this.firstFormGroup);
              }
            }
          )
          .add(() => (this.firstFormLoading = false))
      );
    }
  }

  private logoutAndNavigate(healthDepartment: HealthDepartmentDto): Observable<any> {
    return of(healthDepartment).pipe(
      tap((hd) => this.userService.logout()),
      tap((hd) =>
        this.router.navigate(['/client/enrollment/health-department'], {
          queryParams: { healthDepartment: encodeURIComponent(JSON.stringify(hd)) },
        })
      )
    );
  }

  private handleUnprocessableEntityError(error: ZipCodeErrorDto): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: {
        abortButtonText: this.translate.instant('BASIC_DATA.ABBRECHEN'),
        confirmButtonText: this.translate.instant('BASIC_DATA.PLZ_BESTAETIGEN'),
        title: this.translate.instant('BASIC_DATA.PLZ_UEBERPRUEFEN'),
        text: error.zipCode.message,
      },
    });

    this.subs.add(
      dialogRef
        .afterClosed()
        .subscribe((result) => {
          if (result) {
            this.checkAndSendFirstForm(true);
          } else if (this.stepper.selectedIndex === 1) {
            this.stepper.previous();
          }
        })
        .add(() => (this.firstFormLoading = false))
    );
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
      guessedDateOfInfection: new FormControl(this.firstQuery.guessedDateOfInfection),
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
          .pipe(
            take(1),
            tap((result) => (this.firstQuery = this.secondFormGroup.value)),
            finalize(() => {
              this.secondFormLoading = false;
            })
          )
          .subscribe(
            (result) => {
              this.snackbarService.success('BASIC_DATA.FRAGEBOGEN_GESPEICHERT');
              if (this.showThirdStep) {
                this.stepper.next();
              } else {
                this.snackbarService.success('BASIC_DATA.REGISTRIERUNG_ABGESCHLOSSEN');
                this.router.navigate(['/client/diary/diary-list']);
              }
            },
            (error) => {
              this.badRequestService.handleBadRequestError(error, this.secondFormGroup);
            }
          )
      );
    }
  }

  // ########## STEP III ##########

  buildThirdForm() {
    let day = new Date(this.today);
    this.datesForRetrospectiveContacts = [];

    let firstDay: Date;
    if (!this.secondFormGroup.value.dayOfFirstSymptoms) {
      firstDay = DateFunctions.addDays(this.today, -7);
    } else {
      const firstSymptomsDay = new Date(Date.parse(this.secondFormGroup.value.dayOfFirstSymptoms));
      if (!firstSymptomsDay || firstSymptomsDay > DateFunctions.addDays(this.today, -5)) {
        firstDay = DateFunctions.addDays(this.today, -7);
      } else {
        firstDay = DateFunctions.addDays(firstSymptomsDay, -2);
      }
    }

    while (DateFunctions.getDateWithoutTime(day) >= DateFunctions.getDateWithoutTime(firstDay)) {
      this.datesForRetrospectiveContacts.push(day);
      day = DateFunctions.addDays(day, -1);
    }

    this.datesForRetrospectiveContacts.forEach((day, index) => {
      this.encounters
        .filter((e) => e.date === DateFunctions.getDateWithoutTime(day))
        .forEach((encounter) => {
          this.addEncounterForm(day, index, encounter);
        });
    });
  }

  hasRetrospectiveContacts(): boolean {
    return this.encounters.length > 0;
  }

  onComplete() {
    this.thirdFormLoading = true;
    if (!this.hasRetrospectiveContacts()) {
      const dialogRef = this.dialog.open(TranslatedConfirmationDialogComponent, {
        data: {
          abortButtonText: 'BASIC_DATA.ABBRECHEN',
          confirmButtonText: 'BASIC_DATA.OK',
          title: 'BASIC_DATA.KEINE_RELEVANTEN_KONTAKTE',
          text: 'BASIC_DATA.SIE_HABEN_NOCH_KEINE_RELEVANTEN_KONTAKTE_ERFASST',
        },
      });

      this.subs.add(
        dialogRef
          .afterClosed()
          .subscribe((result) => {
            if (result) {
              this.completeEnrollment(true);
            }
          })
          .add(() => (this.thirdFormLoading = false))
      );
    } else {
      this.completeEnrollment(false);
    }
  }

  private completeEnrollment(withoutEncounters: boolean) {
    this.clientStore.completeEnrollment(withoutEncounters);
    this.subs.add(
      this.clientStore.enrollmentStatus$
        .subscribe((result) => {
          this.snackbarService.success('BASIC_DATA.REGISTRIERUNG_ABGESCHLOSSEN');
          if (result.completedContactRetro) {
            this.router.navigate(['/client/diary/diary-list']);
          }
        })
        .add(() => (this.thirdFormLoading = false))
    );
  }

  addEncounterForm(date: Date, index: number, encounter: EncounterEntry = null): void {
    const componentFactory: ComponentFactory<EncounterFormComponent> = this.componentFactoryResolver.resolveComponentFactory(
      EncounterFormComponent
    );
    const currentContainer = this.containerQueryList.toArray()[index];
    const refs = this.encounterForms.get(date) || [];

    const componentRef = currentContainer.createComponent(componentFactory);

    componentRef.instance.date = date.toISOString();
    componentRef.instance.contactPersons = this.contactPersons;
    componentRef.instance.locations = this.locations;
    if (encounter) {
      componentRef.instance.encounterEntry = encounter;
    }

    this.subs.add(
      componentRef.instance.contactPersonAdded.subscribe((contactPerson) => {
        this.contactPersons.push(contactPerson);
        this.encounterForms.forEach((refs) => {
          refs.forEach((form) => (form.instance.contactPersons = this.contactPersons));
        });
      })
    );

    this.subs.add(
      componentRef.instance.locationAdded.subscribe((location) => {
        this.locations.push(location);
        this.encounterForms.forEach((refs) => {
          refs.forEach((form) => (form.instance.locations = this.locations));
        });
      })
    );

    this.subs.add(
      componentRef.instance.deleteForm.subscribe((encounter) => {
        if (encounter) {
          this.enrollmentService.deleteEncounter(encounter).subscribe(
            () => {
              this.snackbarService.success('BASIC_DATA.KONTAKT_ENTFERNT');
              this.encounters = this.encounters.filter((e) => e !== encounter);
            },
            (error) => {
              this.badRequestService.handleBadRequestError(error, componentRef.instance.formGroup);
            }
          );
        }
        currentContainer.remove(currentContainer.indexOf(componentRef.hostView));
        refs.splice(refs.indexOf(componentRef), 1);
      })
    );

    this.subs.add(
      componentRef.instance.submitForm.subscribe((submitValue: { id: string; encounter: EncounterCreateDto }) => {
        if (submitValue.id) {
          this.enrollmentService.updateEncounter(submitValue.encounter, submitValue.id).subscribe(
            (encounterEntry) => {
              this.snackbarService.success('BASIC_DATA.KONTAKT_GESPEICHERT');
              this.encounters[this.encounters.findIndex((e) => e.id === encounterEntry.id)] = encounterEntry;
              this.encounters = [...this.encounters];
            },
            (error) => {
              this.badRequestService.handleBadRequestError(error, componentRef.instance.formGroup);
            }
          );
        } else {
          this.enrollmentService.createEncounter(submitValue.encounter).subscribe(
            (encounterEntry) => {
              this.snackbarService.success('BASIC_DATA.KONTAKT_GESPEICHERT');
              this.encounters.push(encounterEntry);
              this.encounters = [...this.encounters];
            },
            (error) => {
              this.badRequestService.handleBadRequestError(error, componentRef.instance.formGroup);
            }
          );
        }
      })
    );

    refs.push(componentRef);
    this.encounterForms.set(date, refs);
  }
}
