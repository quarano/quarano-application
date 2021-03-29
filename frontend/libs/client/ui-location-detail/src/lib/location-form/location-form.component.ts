import { BadRequestService } from '@qro/shared/ui-error';
import { ValidationErrorService, VALIDATION_PATTERNS, TrimmedPatternValidator } from '@qro/shared/util-forms';
import { Component, OnInit, Input, Output, EventEmitter, OnDestroy } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { SubSink } from 'subsink';
import { MatInput } from '@angular/material/input';
import { LocationDto, LocationService } from '@qro/client/domain';
import { TranslatedSnackbarService } from '@qro/shared/util-snackbar';
import { map, switchMap } from 'rxjs/operators';

@Component({
  selector: 'qro-location-form',
  templateUrl: './location-form.component.html',
  styleUrls: ['./location-form.component.scss'],
})
export class LocationFormComponent implements OnInit, OnDestroy {
  @Input() location: LocationDto;
  @Output() locationCreated = new EventEmitter<LocationDto>();
  @Output() locationModified = new EventEmitter<any>();
  @Output() cancelled = new EventEmitter<any>();
  @Output() dirty = new EventEmitter<boolean>();

  formGroup: FormGroup;
  private subs = new SubSink();
  loading = false;

  constructor(
    private formBuilder: FormBuilder,
    private snackbarService: TranslatedSnackbarService,
    private badRequestService: BadRequestService,
    public validationErrorService: ValidationErrorService,
    private locationService: LocationService
  ) {}

  ngOnInit() {
    this.buildForm();
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  get isNew() {
    return !this.location.id;
  }

  buildForm() {
    this.formGroup = this.formBuilder.group({
      name: new FormControl(this.location.name, [
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.name),
        Validators.required,
      ]),
      contactPerson: this.formBuilder.group({
        contactPersonName: new FormControl(this.location.contactPerson?.contactPersonName, [
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.name),
        ]),
        contactPersonEmail: new FormControl(this.location.contactPerson?.contactPersonEmail, [
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.email),
        ]),
        contactPersonPhone: new FormControl(this.location.contactPerson?.contactPersonPhone, [
          Validators.minLength(5),
          Validators.maxLength(17),
          TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.phoneNumber),
        ]),
      }),
      street: new FormControl(this.location.street, [
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.street),
      ]),
      houseNumber: new FormControl(this.location.houseNumber, [
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.houseNumber),
      ]),
      zipCode: new FormControl(this.location.zipCode, [
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.zip),
        Validators.required,
      ]),
      city: new FormControl(this.location.city, [
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.city),
        Validators.required,
      ]),
      comment: new FormControl(this.location.comment, [
        TrimmedPatternValidator.trimmedPattern(VALIDATION_PATTERNS.textual),
      ]),
    });
    this.formGroup.valueChanges.subscribe((_) => this.dirty.emit(true));
    // mark all controls with initial value as touched to trigger validation of these values
    this.markControlsWithTruthyValueAsTouched(this.formGroup);
  }

  private markControlsWithTruthyValueAsTouched(group: FormGroup): void {
    Object.keys(group.controls).forEach((key: string) => {
      const abstractControl = group.get(key);
      if (abstractControl instanceof FormGroup) {
        this.markControlsWithTruthyValueAsTouched(abstractControl);
      } else if (abstractControl.value) {
        abstractControl.markAsTouched();
      }
    });
  }

  trimValue(input: MatInput) {
    input.value = input.value?.trim();
  }

  onSubmit() {
    if (this.formGroup.valid) {
      this.loading = true;
      const locationToModify: LocationDto = { ...this.location, ...this.formGroup.value };

      if (this.isNew) {
        this.createLocation(locationToModify);
      } else {
        this.modifyLocation(locationToModify);
      }
    }
  }

  createLocation(location: LocationDto) {
    this.subs.add(
      this.locationService
        .createLocation(location)
        .subscribe(
          (createdLocation) => {
            this.snackbarService.success('LOCATION_FORM.ORT_ANGELEGT');
            this.formGroup.markAsPristine();
            this.locationCreated.emit(createdLocation);
            this.dirty.emit(false);
          },
          (error) => {
            this.badRequestService.handleBadRequestError(error, this.formGroup);
          }
        )
        .add(() => (this.loading = false))
    );
  }

  modifyLocation(location: LocationDto) {
    this.subs.add(
      this.locationService
        .modifyLocation(location, this.location.id)
        .subscribe(
          (_) => {
            this.snackbarService.success('LOCATION_FORM.ORT_AKTUALISIERT');
            this.formGroup.markAsPristine();
            this.locationModified.emit();
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
