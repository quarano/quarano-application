import { BadRequestService } from '@qro/shared/util-error';
import {
  ValidationErrorGenerator,
  VALIDATION_PATTERNS,
  TrimmedPatternValidator,
} from '@qro/shared/util-form-validation';
import { Component, OnInit, Input, Output, EventEmitter, OnDestroy } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { SubSink } from 'subsink';
import { MatInput } from '@angular/material/input';
import { ContactPersonDto, ContactPersonModifyDto, ContactPersonService } from '@qro/client/contact-persons/domain';
import { SnackbarService } from '@qro/shared/util';

@Component({
  selector: 'qro-contact-person-form',
  templateUrl: './contact-person-form.component.html',
  styleUrls: ['./contact-person-form.component.scss'],
})
export class ContactPersonFormComponent implements OnInit, OnDestroy {
  @Input() contactPerson: ContactPersonDto;
  @Output() contactCreated = new EventEmitter<ContactPersonDto>();
  @Output() contactModified = new EventEmitter<any>();
  @Output() cancelled = new EventEmitter<any>();
  @Output() dirty = new EventEmitter<boolean>();
  formGroup: FormGroup;
  private subs = new SubSink();
  showIdentificationHintField = false;
  loading = false;
  errorGenerator = ValidationErrorGenerator;

  constructor(
    private formBuilder: FormBuilder,
    private contactPersonService: ContactPersonService,
    private snackbarService: SnackbarService,
    private badRequestService: BadRequestService
  ) {}

  ngOnInit() {
    this.buildForm();
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  get isNew() {
    return !this.contactPerson.id;
  }

  buildForm() {
    this.formGroup = this.formBuilder.group({
      firstName: new FormControl(this.contactPerson.firstName, [
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.name),
      ]),
      lastName: new FormControl(this.contactPerson.lastName, [
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.name),
      ]),
      email: new FormControl(this.contactPerson.email, [
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.email),
      ]),
      phone: new FormControl(this.contactPerson.phone, [
        Validators.minLength(5),
        Validators.maxLength(17),
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.phoneNumber),
      ]),
      mobilePhone: new FormControl(this.contactPerson.mobilePhone, [
        Validators.minLength(5),
        Validators.maxLength(17),
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.phoneNumber),
      ]),
      street: new FormControl(this.contactPerson.street, [
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.street),
      ]),
      houseNumber: new FormControl(this.contactPerson.houseNumber, [
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.houseNumber),
      ]),
      zipCode: new FormControl(this.contactPerson.zipCode, [
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.zip),
      ]),
      city: new FormControl(this.contactPerson.city, [
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.city),
      ]),
      identificationHint: new FormControl(this.contactPerson.identificationHint, [
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.textual),
      ]),
      isHealthStaff: new FormControl(this.contactPerson.isHealthStaff),
      isSenior: new FormControl(this.contactPerson.isSenior),
      hasPreExistingConditions: new FormControl(this.contactPerson.hasPreExistingConditions),
      remark: new FormControl(this.contactPerson.remark, [
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.textual),
      ]),
    });
    this.formGroup.valueChanges.subscribe((_) => this.dirty.emit(true));
    if (this.contactPerson.identificationHint) {
      this.showIdentificationHintField = true;
    }
  }

  trimValue(input: MatInput) {
    input.value = input.value?.trim();
  }

  private isWayToContactSet(): boolean {
    return (
      this.formGroup.controls.email.value ||
      this.formGroup.controls.phone.value ||
      this.formGroup.controls.mobilePhone.value ||
      this.formGroup.controls.identificationHint.value
    );
  }

  onSubmit() {
    if (this.formGroup.valid) {
      this.loading = true;
      if (this.isWayToContactSet()) {
        const contactPersonToModify = Object.assign(this.formGroup.value);

        if (this.isNew) {
          this.createContactPerson(contactPersonToModify);
        } else {
          this.modifyContactPerson(contactPersonToModify);
        }
      } else if (!this.showIdentificationHintField) {
        this.snackbarService.confirm(
          'Bitte geben Sie mindestens eine Kontaktmöglichkeit oder Hinweise zur Identifikation ein'
        );
        this.showIdentificationHintField = true;
        this.loading = false;
      } else {
        this.snackbarService.confirm(
          'Bitte geben Sie mindestens eine Kontaktmöglichkeit oder Hinweise zur Identifikation ein'
        );
        this.loading = false;
      }
    }
  }

  createContactPerson(contactPerson: ContactPersonModifyDto) {
    this.subs.add(
      this.contactPersonService
        .createContactPerson(contactPerson)
        .subscribe(
          (createdContactPerson) => {
            this.snackbarService.success('Kontaktperson erfolgreich angelegt');
            this.formGroup.markAsPristine();
            this.contactCreated.emit(createdContactPerson);
            this.dirty.emit(false);
          },
          (error) => {
            this.badRequestService.handleBadRequestError(error, this.formGroup);
          }
        )
        .add(() => (this.loading = false))
    );
  }

  modifyContactPerson(contactPerson: ContactPersonModifyDto) {
    this.subs.add(
      this.contactPersonService
        .modifyContactPerson(contactPerson, this.contactPerson.id)
        .subscribe(
          (_) => {
            this.snackbarService.success('Kontaktperson erfolgreich aktualisiert');
            this.formGroup.markAsPristine();
            this.contactModified.emit();
            this.dirty.emit(false);
          },
          (error) => {
            this.badRequestService.handleBadRequestError(error, this.formGroup);
          }
        )
        .add(() => (this.loading = false))
    );
  }

  cancel() {
    this.cancelled.emit();
  }
}
